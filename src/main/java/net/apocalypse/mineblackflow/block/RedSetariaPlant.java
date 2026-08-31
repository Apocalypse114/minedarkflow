package net.apocalypse.mineblackflow.block;

import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.IPlantable;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class RedSetariaPlant extends BaseEntityBlock implements IPlantable {
    public static final IntegerProperty COUNT = IntegerProperty.create("count", 1, 4);

    public RedSetariaPlant() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.TERRACOTTA_RED).noCollission().instabreak()
                .sound(SoundType.GRASS)
                .ignitedByLava().pushReaction(PushReaction.DESTROY));
    }

    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(COUNT)){
            case 1 -> SHAPE_1;
            case 2 -> SHAPE_2;
            default -> SHAPE_3_4;
        };
    }

    protected static final VoxelShape SHAPE_1 = Block.box(4, 0.0D, 4, 12, 14.0D, 12);
    protected static final VoxelShape SHAPE_2 = Block.box(4, 0.0D, 4, 12, 16.0D, 12);
    protected static final VoxelShape SHAPE_3_4 = Block.box(2, 0.0D, 2, 14, 16.0D, 14);

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if (blockstate.is(this)) {
            return blockstate.setValue(COUNT, Math.min(4, blockstate.getValue(COUNT) + 1));
        }
        return super.getStateForPlacement(pContext);
    }

    @Nullable
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState){
        return MBFEntities.RED_SETARIA.get().create(pPos, pState);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(COUNT);
    }
    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return !pState.canSurvive(pLevel, pCurrentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.below();
        if (pState.getBlock() == this) //Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
            return pLevel.getBlockState(blockpos).canSustainPlant(pLevel, blockpos, Direction.UP, this);
        return this.mayPlaceOn(pLevel.getBlockState(blockpos), pLevel, blockpos);
    }

    public boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return pState.is(BlockTags.DIRT) || pState.is(Blocks.FARMLAND);
    }
    public BlockState getPlant(BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != this) return defaultBlockState();
        return state;
    }
}
