package net.apocalypse.mineblackflow.block;

import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class BlackflowStoneBlock extends Block {
    public static final IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

    public BlackflowStoneBlock() {
        super(Properties.of()
                .mapColor(MapColor.STONE)
                .instrument(NoteBlockInstrument.BASEDRUM)
                .requiresCorrectToolForDrops()
                .strength(1.5F, 7.5F));
    }

    @Nullable
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext){
        LevelAccessor levelAccessor = pContext.getLevel();
        return this.defaultBlockState().setValue(VARIANT, Mth.nextInt(levelAccessor.getRandom(), 0, 2));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(VARIANT);
    }
}
