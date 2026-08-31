package net.apocalypse.mineblackflow.block;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.LightEngine;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlackflowGrassBlock extends SpreadingSnowyDirtBlock implements BonemealableBlock {
    public BlackflowGrassBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.GRASS).randomTicks()
                .strength(0.6F).sound(SoundType.GRASS));
    }
    public boolean isValidBonemealTarget(LevelReader p_256229_, BlockPos p_256432_, BlockState p_255677_, boolean p_256630_) {
        return p_256229_.getBlockState(p_256432_.above()).isAir();
    }

    public boolean isBonemealSuccess(Level p_221275_, RandomSource p_221276_, BlockPos p_221277_, BlockState p_221278_) {
        return true;
    }

    public void performBonemeal(ServerLevel p_221270_, RandomSource p_221271_, BlockPos p_221272_, BlockState p_221273_){

    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState pState, @NotNull Direction pFacing,
                                           @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel,
                                           @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (pFacing == Direction.UP && isSnowySetting(pFacingState)) return pState.setValue(SNOWY, true);
        return pState;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void randomTick(@NotNull BlockState pState, @NotNull ServerLevel pLevel,
                           @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (!isSnowySetting(pLevel.getBlockState(pPos.above()))){
            pState.setValue(SNOWY, false);
        }
        if (!canBeGrass(pState, pLevel, pPos)) {
            if (!pLevel.isAreaLoaded(pPos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            pLevel.setBlockAndUpdate(pPos, MBFBlocks.BLACKFLOW_DIRT.get().defaultBlockState());
        } else {
            if (!pLevel.isAreaLoaded(pPos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (pLevel.getMaxLocalRawBrightness(pPos.above()) >= 9) {
                BlockState blockstate = this.defaultBlockState();

                for(int i = 0; i < 4; ++i) {
                    BlockPos blockpos = pPos.offset(pRandom.nextInt(3) - 1, pRandom.nextInt(5) - 3, pRandom.nextInt(3) - 1);
                    if (pLevel.getBlockState(blockpos).is(MBFBlocks.BLACKFLOW_DIRT.get())
                            && canPropagate(blockstate, pLevel, blockpos)) {
                        pLevel.setBlockAndUpdate(blockpos,
                                blockstate.setValue(SNOWY, pLevel.getBlockState(blockpos.above()).is(Blocks.SNOW)));
                    }
                }
            }

        }
    }

    public static boolean canBeGrass(BlockState pState, LevelReader pLevelReader, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        BlockState blockstate = pLevelReader.getBlockState(blockpos);
        if (blockstate.is(Blocks.SNOW) && blockstate.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        } else if (blockstate.getFluidState().getAmount() == 8) {
            return false;
        } else {
            int i = LightEngine.getLightBlockInto(pLevelReader, pState, pPos, blockstate, blockpos, Direction.UP, blockstate.getLightBlock(pLevelReader, blockpos));
            return i < pLevelReader.getMaxLightLevel();
        }
    }
    public static boolean isSnowySetting(BlockState pState) {
        return pState.is(BlockTags.SNOW);
    }

    public static boolean canPropagate(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        return canBeGrass(pState, pLevel, pPos) && !pLevel.getFluidState(blockpos).is(FluidTags.WATER);
    }
}
