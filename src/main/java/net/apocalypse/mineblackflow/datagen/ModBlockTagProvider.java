package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MineBlackFlow.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                MBFBlocks.BLACKFLOWIUM_CLUSTER.get(),
                MBFBlocks.BLACKFLOWIUM_BLOCK.get(),
                MBFBlocks.BLACKFLOW_STONE.get(),
                MBFBlocks.BLACKFLOW_STONE_SET.stair().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.slab().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.wall().get()
        );
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                MBFBlocks.BLACKFLOW_GRASS_BLOCK.get(),
                MBFBlocks.BLACKFLOW_DIRT.get()
        );
        this.tag(BlockTags.ANIMALS_SPAWNABLE_ON).add(
                MBFBlocks.BLACKFLOW_GRASS_BLOCK.get()
        );
        this.tag(BlockTags.DIRT).add(
                MBFBlocks.BLACKFLOW_GRASS_BLOCK.get(),
                MBFBlocks.BLACKFLOW_DIRT.get()
        );
        this.tag(BlockTags.ENDERMAN_HOLDABLE).add(
                MBFBlocks.BLACKFLOW_GRASS_BLOCK.get(),
                MBFBlocks.BLACKFLOW_DIRT.get()
        );
        this.tag(BlockTags.BEACON_BASE_BLOCKS).add(
                MBFBlocks.BLACKFLOWIUM_BLOCK.get()
        );
        this.tag(BlockTags.WALLS).add(
                MBFBlocks.BLACKFLOW_STONE_SET.stair().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.slab().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.wall().get()
        );
        this.tag(BlockTags.STAIRS).add(
                MBFBlocks.BLACKFLOW_STONE_SET.stair().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.slab().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.wall().get()
        );
        this.tag(BlockTags.SLABS).add(
                MBFBlocks.BLACKFLOW_STONE_SET.stair().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.slab().get(),
                MBFBlocks.BLACKFLOW_STONE_SET.wall().get()
        );
    }
}
