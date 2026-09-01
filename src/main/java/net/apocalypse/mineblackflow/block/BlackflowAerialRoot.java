package net.apocalypse.mineblackflow.block;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;

public class BlackflowAerialRoot extends LadderBlock {
    public BlackflowAerialRoot() {
        super(BlockBehaviour.Properties.of()
                .forceSolidOff()
                .strength(0.5F).sound(SoundType.MANGROVE_ROOTS)
                .noOcclusion().pushReaction(PushReaction.DESTROY));
    }
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pState.getValue(WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.getTickDelay(pLevel));
        }
        Direction myDirection = pState.getValue(FACING);
        if (pFacing != myDirection.getOpposite() && !canSurvive(pState, pLevel, pCurrentPos)){
            Direction ROT_1 = myDirection.getClockWise(), ROT_2 = ROT_1.getClockWise(), ROT_3 = ROT_2.getClockWise();
            BlockState stateRot1 = pState.setValue(FACING, ROT_1),
                    stateRot2 = pState.setValue(FACING, ROT_2),
                    stateRot3 = pState.setValue(FACING, ROT_3);
            if (canSurvive(stateRot1, pLevel, pCurrentPos)) return stateRot1.setValue(FACING, ROT_1.getClockWise());
            if (canSurvive(stateRot2, pLevel, pCurrentPos)) return stateRot2.setValue(FACING, ROT_2.getClockWise());
            if (canSurvive(stateRot3, pLevel, pCurrentPos)) return stateRot3.setValue(FACING, ROT_3.getClockWise());
            return Blocks.AIR.defaultBlockState();
        }
        return pState;
    }

    @Override
    public boolean isLadder(BlockState state, LevelReader world, BlockPos pos, LivingEntity entity) {
        return true;
    }


    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return canAttach(pLevel, pPos, pState.getValue(FACING)) || isLinked(pPos, pLevel, pState);
    }

    public boolean isAttached(Direction targetDirection, BlockPos myPos, BlockGetter plevel){
        BlockPos targetPos = myPos.relative(targetDirection);
        BlockState targetState = plevel.getBlockState(targetPos);
        return targetState.isFaceSturdy(plevel, targetPos, targetDirection.getOpposite());
    }
    public boolean canAttach(LevelReader pLevel, BlockPos pPos, Direction direction){
        return isAttached(Direction.UP, pPos, pLevel)
                || isAttached(Direction.DOWN, pPos, pLevel)
                || isAttached(direction.getOpposite(), pPos, pLevel)
                || isAttached(direction.getClockWise(), pPos, pLevel)
                || isAttached(direction.getCounterClockWise(), pPos, pLevel);
    }
    public boolean isLinked(BlockPos myPos, LevelReader level, BlockState myState){
        BlockPos abovePos = myPos.above(), belowPos = myPos.below();
        BlockState aboveState = level.getBlockState(abovePos), belowState = level.getBlockState(belowPos);
        return belowState.getBlock() == myState.getBlock() || aboveState.getBlock() == myState.getBlock();
    }
}
