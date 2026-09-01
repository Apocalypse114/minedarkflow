package net.apocalypse.mineblackflow.datagen;

import net.apocalypse.mineblackflow.block.BlackflowPropaguleBlock;
import net.apocalypse.mineblackflow.MineBlackFlow;
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
       simpleBlockWithItem(MBFBlocks.BLACKFLOW_LEAVE.get(), models().leaves("blackflow_leave", MineBlackFlow.modLoc("block/blackflow_leave")));
       multiStateBlock(MBFBlocks.BLACKFLOW_PROPAGULE.get(), state -> {
           int age = state.getValue(BlackflowPropaguleBlock.AGE);
           return models()
                   .withExistingParent("blackflow_propagule_"+age, MineBlackFlow.modLoc("custom/blackflow_tree_root"))
                   .texture("0", MineBlackFlow.modLoc("block/blackflow_tree_sapling_"+age))
                   .renderType("cutout");
       });
       simpleBlockItem(MBFBlocks.BLACKFLOW_PROPAGULE.get(), itemModels().basicItem(MineBlackFlow.modLoc("blackflow_tree_propagule")));
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