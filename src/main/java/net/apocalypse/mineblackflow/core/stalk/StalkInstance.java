package net.apocalypse.mineblackflow.core.stalk;

import com.mojang.datafixers.util.Pair;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.data.DimensionData;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.*;

@SuppressWarnings("removal")
public class StalkInstance{
    private final BlockPos centerPos;
    private int tickCount = 0;
    private final StalkCast cast;
    private final ResourceLocation castId;
    private boolean done = false;

    private final PriorityQueue<Pair<Integer, StalkCast.EnemyGroupEntry>> CACHE = new PriorityQueue<>(Comparator.comparingInt(Pair::getFirst));

    private StalkInstance(ResourceLocation castId, BlockPos pos, int tickCount){
        this(castId, pos);
        this.tickCount = tickCount;
    }
    public StalkInstance(ResourceLocation castId, BlockPos pos){
        this.centerPos = pos;
        this.cast = StalkCast.MANAGER.getCast(castId);
        this.castId = castId;
    }

    public boolean validInstance(){return cast != null;}

    public void tick(ServerLevel level){
        if (this.cast == null){
            done = true;
            return;
        }
        if (!done && level.isLoaded(centerPos)) {
            if (CACHE.isEmpty()) computeCast();
            while (true) {
                var nextEntry = CACHE.peek();
                if (nextEntry == null) break;
                if (tickCount == nextEntry.getFirst()) {
                    var item = CACHE.poll();
                    if (item == null) break;
                    summonEnemyGroup(item.getSecond(), level);
                } else if (tickCount > nextEntry.getFirst()) {
                    CACHE.poll();
                } else break;
            }
            if (CACHE.isEmpty()) done = true;
            tickCount++;
        }
    }
    public boolean isDone(){return done;}

    private void computeCast(){
        for (StalkCast.EnemyGroupEntry entry: cast.getGroups()){
            CACHE.add(Pair.of(entry.occurrenceTick(), entry));
        }
    }
    private void summonEnemyGroup(StalkCast.EnemyGroupEntry entry, ServerLevel level){
        double d = entry.nextDist(level);
        int count = entry.count();
        EntityType<?> type = entry.type();
        if (type == null) return;
        float prob = entry.possibilityToBeNullMasked();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i<count; i++){
            int angle = Mth.nextInt(level.getRandom(), 0, 360);
            double dx = d * Mth.sin(angle * Mth.DEG_TO_RAD), dz = d * Mth.cos(angle * Mth.DEG_TO_RAD);
            double y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int) dx, (int) dz);
            pos.set(centerPos.getX() + dx, y, centerPos.getZ() + dz);
            Entity summoned = type.spawn(level, pos, MobSpawnType.MOB_SUMMONED);
            if (summoned instanceof LivingEntity living){
                if (living.getRandom().nextFloat() < prob) MBFUtil.makeNullMasked(living);
                living.setYRot(living.getRandom().nextFloat() * 360 * Mth.DEG_TO_RAD);
                setInStalk(summoned);
            }
        }
    }

    public BlockPos getPos(){
        return centerPos;
    }
    public int tickCount(){return tickCount;}
    public StalkCast getCast(){return cast;}

    private static final String TAG_POS = "center_pos";
    private static final String TAG_TICK_COUNT = "tick_count";
    private static final String TAG_CAST = "cast_id";

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_POS, NbtUtils.writeBlockPos(centerPos));
        tag.putInt(TAG_TICK_COUNT, tickCount);
        tag.putString(TAG_CAST, castId.toString());
        return tag;
    }
    public static StalkInstance fromNBT(CompoundTag nbt){
        BlockPos centerPos = NbtUtils.readBlockPos(nbt.getCompound(TAG_POS));
        int tickCount = nbt.getInt(TAG_TICK_COUNT);
        String castId = nbt.getString(TAG_CAST);
        return new StalkInstance(new ResourceLocation(castId), centerPos, tickCount);
    }

    public static final String TAG_IN_STALK = MineBlackFlow.modTagName("in_stalk");

    public static boolean isInStalk(Entity entity){
        return entity.getPersistentData().getBoolean(TAG_IN_STALK);
    }
    public static void setInStalk(Entity entity){
        entity.getPersistentData().putBoolean(TAG_IN_STALK, true);
    }
}
