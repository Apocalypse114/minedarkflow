package net.apocalypse.mineblackflow.core.stalk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class StalkCast {
    public static final StalkCastManager MANAGER = new StalkCastManager();

    public static final Codec<EnemyGroupEntry> ENEMY_GROUP_ENTRY_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ForgeRegistries.ENTITY_TYPES.getCodec().fieldOf("type").forGetter(EnemyGroupEntry::type),
                    Codec.INT.fieldOf("count").forGetter(EnemyGroupEntry::count),
                    Codec.INT.fieldOf("occurrenceTick").forGetter(EnemyGroupEntry::occurrenceTick),
                    Codec.DOUBLE.fieldOf("minDist").forGetter(EnemyGroupEntry::minDist),
                    Codec.DOUBLE.fieldOf("maxDist").forGetter(EnemyGroupEntry::maxDist),
                    Codec.FLOAT.fieldOf("nullMaskProb").forGetter(EnemyGroupEntry::possibilityToBeNullMasked)
            ).apply(instance, EnemyGroupEntry::new)
    );
    public static final Codec<StalkCast> STALK_CAST_CODEC = ENEMY_GROUP_ENTRY_CODEC.listOf().xmap(StalkCast::new, StalkCast::getGroups);

    private final List<EnemyGroupEntry> groups;

    private ResourceLocation location = null;
    private String descId = "";

    public List<EnemyGroupEntry> getGroups(){return groups;}
    public int getSize(){return groups.size();}
    public ResourceLocation getLocation(){return location;}
    protected void setLocation(@NotNull ResourceLocation location1){
        location = location1;
        descId = "stalk.mine_black_flow."+location1.getPath();
    }
    public MutableComponent getDesc(){return Component.translatable(descId);}

    public StalkCast(List<EnemyGroupEntry> entries){
        this.groups = new ArrayList<>(entries);
    }
    public EnemyGroupEntry getGroup(int index){
        index = Mth.clamp(index, 0, groups.size()-1);
        return groups.get(index);
    }
    public static StalkCast create(){
        return new StalkCast(List.of());
    }

    public @NotNull StalkCast addEntry(EnemyGroupEntry entry){
        this.groups.add(entry);
        return this;
    }
    public @NotNull StalkCast addEntry(EntityType<?> type, int count, int occurrenceTick, double minDist, double maxDist, float nullMaskProb){
        return addEntry(new EnemyGroupEntry(type, count, occurrenceTick, minDist, maxDist, nullMaskProb));
    }

    public record EnemyGroupEntry(EntityType<?> type, int count, int occurrenceTick, double minDist, double maxDist, float possibilityToBeNullMasked){
        public double nextDist(Level pLevel){
            if (minDist > maxDist) return minDist;
            return Mth.nextDouble(pLevel.getRandom(), minDist, maxDist);
        }
    }
}
