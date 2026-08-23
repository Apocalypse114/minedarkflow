package net.apocalypse.mineblackflow.core.stalk;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
    public static final Codec<StalkCast> STALK_CAST_CODEC = ENEMY_GROUP_ENTRY_CODEC.listOf().xmap(StalkCast::new, StalkCast::getCasts);

    private final List<EnemyGroupEntry> casts;

    public List<EnemyGroupEntry> getCasts(){return casts;}
    public int getSize(){return casts.size();}

    public StalkCast(List<EnemyGroupEntry> entries){
        this.casts = new ArrayList<>(entries);
    }
    public EnemyGroupEntry getGroup(int index){
        index = Mth.clamp(index, 0, casts.size()-1);
        return casts.get(index);
    }
    public static StalkCast create(){
        return new StalkCast(List.of());
    }

    public @NotNull StalkCast addEntry(EnemyGroupEntry entry){
        this.casts.add(entry);
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
