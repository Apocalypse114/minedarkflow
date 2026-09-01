package net.apocalypse.mineblackflow.block;

import net.apocalypse.mineblackflow.init.MBFBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlackflowPropaguleBlock extends BushBlock implements SimpleWaterloggedBlock {

    public BlackflowPropaguleBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
                .sound(SoundType.GRASS)
                .pushReaction(PushReaction.DESTROY));
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.above();
        return pLevel.getBlockState(blockpos).getBlock() == MBFBlocks.BLACKFLOW_LEAVE.get();
    }

    @Override
    public void randomTick(BlockState pState, ServerLevel pLevel, BlockPos pPos, RandomSource pRandom) {
        super.randomTick(pState, pLevel, pPos, pRandom);
        int age = pState.getValue(AGE);
        if (age < 3) pLevel.setBlockAndUpdate(pPos, pState.setValue(AGE, age+1));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Vec3 vec3 = pState.getOffset(pLevel, pPos);
        VoxelShape voxelshape = SHAPE_PER_AGE[pState.getValue(AGE)];
        return voxelshape.move(vec3.x, vec3.y, vec3.z);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        FluidState fluidstate = pContext.getLevel().getFluidState(pContext.getClickedPos());
        boolean flag = fluidstate.getType() == Fluids.WATER;
        return super.getStateForPlacement(pContext).setValue(WATERLOGGED, flag).setValue(AGE, 0);
    }

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPE_PER_AGE = new VoxelShape[]{
            Block.box(6, 11, 6, 10, 16, 10),
            Block.box(5.5D, 8, 5.5D, 10.5D, 16, 10.5D),
            Block.box(5, 3, 5, 11, 16, 11),
            Block.box(4.5D, 1, 4.5D, 11.5D, 16, 11.5D)
    };
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE).add(WATERLOGGED);
    }
}
