package net.apocalypse.mineblackflow.block;

import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class BlackflowLeaveBlock extends LeavesBlock {
    public BlackflowLeaveBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F).randomTicks()
                .sound(SoundType.GRASS).noOcclusion()
                .isValidSpawn(BlackflowLeaveBlock::isValidSpawn)
                .isSuffocating(BlackflowLeaveBlock::never)
                .isViewBlocking(BlackflowLeaveBlock::never)
                .ignitedByLava().pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor(BlackflowLeaveBlock::never));
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
        return adjacentBlockState.getBlock() == this || super.skipRendering(state, adjacentBlockState, side);
    }


    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        BlockPos belowPos = pPos.below();
        if (checkPropaguleDensity(pLevel, pPos) && pLevel.getBlockState(belowPos).canBeReplaced()){
            boolean water = pLevel.getFluidState(belowPos).is(Fluids.WATER);
            pLevel.setBlockAndUpdate(pPos.below(),
                    MBFBlocks.BLACKFLOW_PROPAGULE.get().defaultBlockState().setValue(BlackflowPropaguleBlock.WATERLOGGED, water));
        }
    }

    public static boolean checkPropaguleDensity(ServerLevel level, BlockPos pos){
        BlockPos.MutableBlockPos foundPos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        for (int i = -1; i<=1; i++){
            for (int j = -1; j<=1; j++){
                for (int k = -1; k <= 1; k++){
                    foundPos.setWithOffset(pos, i, j, k);
                    if (level.getBlockState(foundPos).is(MBFBlocks.BLACKFLOW_PROPAGULE.get())) return false;
                }
            }
        }
        return true;
    }

    private static Boolean isValidSpawn(BlockState p_50822_, BlockGetter p_50823_, BlockPos p_50824_, EntityType<?> p_50825_) {
        return p_50825_ == EntityType.OCELOT || p_50825_ == EntityType.PARROT || p_50825_ instanceof IBlackFlowMonster;
    }
    private static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }


}
