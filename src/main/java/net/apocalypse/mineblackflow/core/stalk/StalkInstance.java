package net.apocalypse.mineblackflow.core.stalk;

import com.mojang.datafixers.util.Pair;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.LivingData;
import net.apocalypse.mineblackflow.client.bossbar.CustomBossBarHandler;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.*;

@SuppressWarnings("removal")
public class StalkInstance{
    private final BlockPos centerPos;
    private final Vec3 centerPosVec3;
    private int tickCount = 0;
    private final StalkCast cast;
    private final ResourceLocation castId;
    private boolean done = false;
    private boolean spawningDone = false;
    private final List<LivingEntity> entityInStalk = new ArrayList<>();
    private final List<UUID> entityUUIDInStalk = new ArrayList<>();

    private final Component name;
    private final ServerBossEvent stalkBar = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    private boolean isEntityDataReserved = false;
    private float biggestHistoryMaxHealth = 0;

    private final PriorityQueue<Pair<Integer, StalkCast.EnemyGroupEntry>> CACHE = new PriorityQueue<>(Comparator.comparingInt(Pair::getFirst));

    private StalkInstance(ResourceLocation castId, BlockPos pos, int tickCount){
        this(castId, pos);
        this.tickCount = tickCount;
    }
    public StalkInstance(ResourceLocation castId, BlockPos pos){
        this.centerPos = pos;
        this.centerPosVec3 = pos.getCenter();
        this.cast = StalkCast.MANAGER.getCast(castId);
        this.castId = castId;
        this.name = cast != null? cast.getDesc(): Component.empty();
        CustomBossBarHandler.put(stalkBar, CustomBossBarHandler.TEST_RENDERER);
    }

    public boolean validInstance(){return cast != null;}

    private int preparationTick = 0;
    public void tick(ServerLevel level){
        if (this.cast == null){
            done = true;
            return;
        }
        if (preparationTick < 5){
            preparationTick ++;
            return;
        }
        if (!done && level.isLoaded(centerPos)) {
            if (entityInStalk.isEmpty() && !isEntityDataReserved)
                revertEntityList(level);
            if (CACHE.isEmpty())
                computeCast();
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
            if (CACHE.isEmpty()) spawningDone = true;
            if (tickCount % 5 == 3){
                tickEntitiesOrDone();
                tickPlayersAndBar(level);
            }
            tickCount++;
        }
    }
    public boolean isDone(){return done;}
    public void onDone(ServerLevel level){
        if (done){
            stalkBar.removeAllPlayers();
        }
    }
    public void joinStalk(LivingEntity living){
        if (living instanceof Player) return;
        LivingData data = MBFCapabilities.getLivingData(living);
        data.joiningStalk = this;
        data.sendToTracker(living);
        MineBlackFlow.LOGGER.info("successfully join entity: {}", living.getName().getString());
        entityInStalk.add(living);
    }
    private void tickEntitiesOrDone(){
        entityInStalk.remove(null);
        float healthTotal = 0, maxHealthTotal = 0;
        for (LivingEntity entity: new ArrayList<>(entityInStalk)){
            if (entity.isDeadOrDying() || entity.isRemoved()) entityInStalk.remove(entity);
            else {
                healthTotal += entity.getHealth();
                maxHealthTotal += entity.getMaxHealth();
            }
        }
        biggestHistoryMaxHealth = Math.max(maxHealthTotal, biggestHistoryMaxHealth);
        if (healthTotal > 0 && biggestHistoryMaxHealth > 0) {
            float progress = healthTotal / biggestHistoryMaxHealth;
            this.stalkBar.setProgress(progress);
        }
        this.stalkBar.setName(assembleName(entityInStalk.size()));
        if (spawningDone && entityInStalk.isEmpty()) done = true;
    }
    private Component assembleName(int num){
        return Component.translatable("gameplay.mine_black_flow.stalk_title",
                this.name, num, Math.round(tickCount * 0.025f));
    }
    private void tickPlayersAndBar(ServerLevel level){
        for (ServerPlayer player: level.getPlayers(p -> p.distanceToSqr(centerPosVec3) <= 1024)){
            if (!stalkBar.getPlayers().contains(player)) stalkBar.addPlayer(player);
        }
    }

    private void computeCast(){
        for (StalkCast.EnemyGroupEntry entry: cast.getGroups()){
            CACHE.add(Pair.of(entry.occurrenceTick(), entry));
        }
    }
    private void revertEntityList(ServerLevel level){
        for (UUID uuid: entityUUIDInStalk){
            MineBlackFlow.LOGGER.info("revert entity from UUID: {}", uuid.toString());
            Entity entity = level.getEntity(uuid);
            if (entity instanceof LivingEntity living) joinStalk(living);
        }
        isEntityDataReserved = true;
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
                entityInStalk.add(living);
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
    private static final String TAG_LIST = "entity_in_stalk";

    public CompoundTag serializeNBT(){
        CompoundTag tag = new CompoundTag();
        tag.put(TAG_POS, NbtUtils.writeBlockPos(centerPos));
        tag.putInt(TAG_TICK_COUNT, tickCount);
        tag.putString(TAG_CAST, castId.toString());
        ListTag listTag = new ListTag();
        for (Entity entity: entityInStalk){
            listTag.add(StringTag.valueOf(entity.getStringUUID()));
        }
        tag.put(TAG_LIST, listTag);
        return tag;
    }
    public static StalkInstance fromNBT(CompoundTag nbt){
        BlockPos centerPos = NbtUtils.readBlockPos(nbt.getCompound(TAG_POS));
        int tickCount = nbt.getInt(TAG_TICK_COUNT);
        String castId = nbt.getString(TAG_CAST);
        StalkInstance instance = new StalkInstance(new ResourceLocation(castId), centerPos, tickCount);
        ListTag listTag = nbt.getList(TAG_LIST, Tag.TAG_STRING);
        for (Tag entryTag: listTag){
            String strUuid = entryTag.getAsString();
            try {
                UUID uuid = UUID.fromString(strUuid);
                instance.putUUID(uuid);
            } catch (Exception e) {
                MineBlackFlow.LOGGER.warn("Invalid UUID found when reverting stalk instance data: {}", e.getMessage());
            }
        }
        return instance;
    }
    private void putUUID(UUID uuid){
        entityUUIDInStalk.add(uuid);
    }

    public static final String TAG_IN_STALK = MineBlackFlow.modTagName("in_stalk");

    public static boolean isInStalk(Entity entity){
        return entity.getPersistentData().getBoolean(TAG_IN_STALK);
    }
    public static void setInStalk(Entity entity){
        entity.getPersistentData().putBoolean(TAG_IN_STALK, true);
    }
}
