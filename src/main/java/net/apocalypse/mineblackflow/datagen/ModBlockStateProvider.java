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
        simpleBlock(MBFBlocks.BLOOD_MUSHROOM_BLOCK.get(), models().cross("blood_mushroom_plant", MineBlackFlow.modLoc("item/blood_mushroom")));
        simpleBlockItem(MBFBlocks.BLOOD_MUSHROOM_BLOCK.get(), itemModels().basicItem(MineBlackFlow.modLoc("blood_mushroom")));
    }

    private void cubeBlockWithItem(Block block){
        simpleBlockWithItem(block, cubeAll(block));
    }
}