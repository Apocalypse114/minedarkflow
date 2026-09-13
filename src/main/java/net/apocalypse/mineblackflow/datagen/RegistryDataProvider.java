package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.worldgen.biome.MBFBiome;
import net.apocalypse.mineblackflow.worldgen.biome.MBFBiomeModifiers;
import net.apocalypse.mineblackflow.worldgen.dimension.MBFDimensions;
import net.apocalypse.mineblackflow.worldgen.feature.MBFConfiguredFeatures;
import net.apocalypse.mineblackflow.worldgen.feature.MBFPlacedFeatures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder builder = new RegistrySetBuilder()
            .add(Registries.DIMENSION_TYPE, MBFDimensions::bootstrapDimensionType)
            .add(Registries.LEVEL_STEM, MBFDimensions::bootstrapLevelStem)
            .add(Registries.CONFIGURED_FEATURE, MBFConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, MBFPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, MBFBiomeModifiers::bootstrap)
            .add(Registries.BIOME, MBFBiome::bootstrap);

    public RegistryDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, builder, Set.of("minecraft", MineBlackFlow.MODID));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {

    }
}
