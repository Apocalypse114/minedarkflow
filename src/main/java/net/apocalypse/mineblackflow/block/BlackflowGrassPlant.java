package net.apocalypse.mineblackflow.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlackflowGrassPlant extends BushBlock {
    public static final BooleanProperty SHORT = BooleanProperty.create("short");

    public BlackflowGrassPlant() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT).replaceable().noCollission().instabreak()
                .sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XYZ)
                .ignitedByLava().pushReaction(PushReaction.DESTROY));
    }
    public @NotNull VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return pState.getValue(SHORT) ? SHAPE_SHORT: SHAPE;
    }
    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext){
        LevelAccessor levelAccessor = pContext.getLevel();
        return levelAccessor.getRandom().nextFloat() < 0.5f? this.defaultBlockState().setValue(SHORT, false) : this.defaultBlockState().setValue(SHORT, true);
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHORT);
    }
    protected static final VoxelShape SHAPE = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    protected static final VoxelShape SHAPE_SHORT = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 6.0D, 14.0D);
}
