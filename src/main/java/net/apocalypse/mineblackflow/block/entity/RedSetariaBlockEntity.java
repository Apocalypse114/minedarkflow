package net.apocalypse.mineblackflow.block.entity;

import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class RedSetariaBlockEntity extends SimpleGeoBlockEntity{
    public RedSetariaBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(MBFEntities.RED_SETARIA.get(), pPos, pBlockState);
    }
}
