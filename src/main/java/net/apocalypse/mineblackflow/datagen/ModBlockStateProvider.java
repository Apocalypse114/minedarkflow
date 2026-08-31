package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.block.BlackflowGrassBlock;
import net.apocalypse.mineblackflow.block.BlackflowGrassPlant;
import net.apocalypse.mineblackflow.block.BlackflowStoneBlock;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Function;

@SuppressWarnings("SameParameterValue")
public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MineBlackFlow.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        genBuildingBlockSet("blackflow_stone", MBFBlocks.BLACKFLOW_STONE_SET, MineBlackFlow.modLoc("block/blackflow_stone_0"));
    }

    private void cubeBlockWithItem(Block block){
        simpleBlockWithItem(block, cubeAll(block));
    }
    private void multiStateBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> ConfiguredModel.builder()
                        .modelFile(modelFunc.apply(state))
                        .build()
                );
    }
    private void genBuildingBlockSet(String name, MBFBlocks.NonCubeBuildingBlockSet set, ResourceLocation side, ResourceLocation bottom, ResourceLocation top){
        stairsBlock(set.stair().get(), name, side, bottom, top);
        slabBlock(set.slab().get(), side, side, bottom, top);
        wallBlock(set.wall().get(), side);
    }
    private void genBuildingBlockSet(String name, MBFBlocks.NonCubeBuildingBlockSet set, ResourceLocation side){
        genBuildingBlockSet(name, set, side, side, side);
    }
}