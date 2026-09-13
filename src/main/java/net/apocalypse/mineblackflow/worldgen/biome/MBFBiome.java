package net.apocalypse.mineblackflow.worldgen.biome;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.MBFMath;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;

public class MBFBiome {
    public static final ResourceKey<Biome> SHALLOW_BLACKFLOW = create("shallow_blackflow");

    public static void bootstrap(BootstapContext<Biome> context){
        context.register(SHALLOW_BLACKFLOW, shallowBlackflow(context));
    }

    public static Biome shallowBlackflow(BootstapContext<Biome> context){
        MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
        overworldDefaultSpawn(spawnSettings);
        spawnSettings.addSpawn(MobCategory.MONSTER, groupData(MBFEntities.THE_NULL_VALUE.get(), 8, 1, 2));

        BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder(
                context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER));
        overworldDefaultGen(generationSettings);
        BiomeDefaultFeatures.addMossyStoneBlock(generationSettings);
        BiomeDefaultFeatures.addDefaultOres(generationSettings, true);
        BiomeDefaultFeatures.addDefaultExtraVegetation(generationSettings);
        return new Biome.BiomeBuilder()
                .generationSettings(generationSettings.build())
                .mobSpawnSettings(spawnSettings.build())
                .downfall(0.75f).temperature(1)
                .hasPrecipitation(true)
                .temperatureAdjustment(Biome.TemperatureModifier.NONE)
                .specialEffects(new BiomeSpecialEffects.Builder()
                        .foliageColorOverride(MBFMath.DEEP_BLACKFLOW_FOLIAGE_COLOR)
                        .fogColor(0x555555)
                        .waterColor(0x446fe9)
                        .waterFogColor(0x446fe9)
                        .skyColor(0x222222)
                        .build())
                .build();
    }

    public static void overworldDefaultGen(BiomeGenerationSettings.Builder builder){
        BiomeDefaultFeatures.addDefaultCarversAndLakes(builder);
        BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
        BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(builder);
        BiomeDefaultFeatures.addDefaultSprings(builder);
        BiomeDefaultFeatures.addDefaultFlowers(builder);
    }
    public static void overworldDefaultSpawn(MobSpawnSettings.Builder builder){
        BiomeDefaultFeatures.farmAnimals(builder);
        BiomeDefaultFeatures.commonSpawns(builder);
    }

    public static ResourceKey<Biome> create(String name){
        return ResourceKey.create(Registries.BIOME, MineBlackFlow.modLoc(name));
    }
    public static MobSpawnSettings.SpawnerData groupData(EntityType<?> type, int weight, int min, int max){
        return new MobSpawnSettings.SpawnerData(type, weight, min, max);
    }
}
