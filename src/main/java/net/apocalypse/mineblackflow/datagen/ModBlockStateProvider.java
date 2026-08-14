package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MineBlackFlow.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        cubeBlockWithItem(MBFBlocks.BLACKFLOWIUM_BLOCK.get());
        simpleBlock(MBFBlocks.BLACKFLOWIUM_CLUSTER.get(), models()
                .cross("blackflowium_cluster", MineBlackFlow.modLoc("block/blackflowium_cluster"))
                .renderType("cutout"));
        //simpleBlockItem(MBFBlocks.BLACKFLOWIUM_CLUSTER.get(), itemModels().basicItem(MineBlackFlow.modLoc("block/blackflowium_cluster")));
    }

    private void cubeBlockWithItem(Block block){
        simpleBlockWithItem(block, cubeAll(block));
    }
}