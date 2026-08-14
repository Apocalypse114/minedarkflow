package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {
    public ModBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(MBFBlocks.BLACKFLOWIUM_BLOCK.get());
        dropSelf(MBFBlocks.BLACKFLOWIUM_CLUSTER.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return MBFBlocks.REGISTRY.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
