package net.apocalypse.mineblackflow.worldgen.dimension;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.worldgen.biome.MBFBiome;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.FixedBiomeSource;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.common.world.BiomeModifier;

import java.util.OptionalLong;

public class MBFDimensions {
    public static final ResourceKey<Level> BLACKFLOW = ResourceKey.create(Registries.DIMENSION, MineBlackFlow.modLoc("blackflow_arbors"));
    public static final ResourceKey<LevelStem> BLACKFLOW_STEM = ResourceKey.create(Registries.LEVEL_STEM, MineBlackFlow.modLoc("blackflow_stem"));
    public static final ResourceKey<DimensionType> BLACKFLOW_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, MineBlackFlow.modLoc("blackflow_type"));

    private static final DimensionType.MonsterSettings monsterSet = new DimensionType.MonsterSettings(
            true, true, UniformInt.of(0, 7), 7
    );

    public static final DimensionType type = new DimensionType(
            OptionalLong.of(-1), true, false, false,
            true, 1.0, true, false, -64, 320, 320,
            BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 1, monsterSet
    );
    public static void bootstrapDimensionType(BootstapContext<DimensionType> context){
        context.register(BLACKFLOW_TYPE, type);
    }
    public static void bootstrapLevelStem(BootstapContext<LevelStem> context){
        // 从上下文中获取维度类型的 Holder
        HolderGetter<DimensionType> dimensionTypes = context.lookup(Registries.DIMENSION_TYPE);
        // 从上下文中获取噪声设置的 Holder
        HolderGetter<NoiseGeneratorSettings> noiseSettings = context.lookup(Registries.NOISE_SETTINGS);

        // 创建区块生成器 (使用原版的噪声生成器设置)
        NoiseBasedChunkGenerator chunkGenerator = new NoiseBasedChunkGenerator(
                new FixedBiomeSource(context.lookup(Registries.BIOME).getOrThrow(MBFBiome.SHALLOW_BLACKFLOW)),
                noiseSettings.getOrThrow(NoiseGeneratorSettings.LARGE_BIOMES)
        );

        context.register(BLACKFLOW_STEM, new LevelStem(
                dimensionTypes.getOrThrow(BLACKFLOW_TYPE), // 维度类型
                chunkGenerator                                       // 区块生成器
        ));
    }
}
