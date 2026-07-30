package net.apocalypse.mineblackflow.datagen;

import com.mojang.datafixers.util.Pair;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder();

    public static final List<Pair<ResourceKey<DamageType>, DamageType>> dataToGen = new ArrayList<>();

    public static final ResourceKey<DamageType> MANIA_SWALLOW = create("mania_swallow", DamageScaling.NEVER, 0.25f);

    static {
        BUILDER.add(Registries.DAMAGE_TYPE, RegistryDataProvider::bootstrap);
    }

    public RegistryDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of("minecraft", MineBlackFlow.MODID));
    }

    public static void bootstrap(BootstapContext<DamageType> context) {
        for (var entry : dataToGen) {
            context.register(entry.getFirst(), entry.getSecond());
        }
    }

    public static ResourceKey<DamageType> create(String name, DamageScaling scaling, float exhaustion){
        ResourceKey<DamageType> key = ResourceKey.create(Registries.DAMAGE_TYPE, MineBlackFlow.modLoc(name));
        DamageType type = new DamageType(name, scaling, exhaustion);
        dataToGen.add(Pair.of(key, type));
        return key;
    }
}
