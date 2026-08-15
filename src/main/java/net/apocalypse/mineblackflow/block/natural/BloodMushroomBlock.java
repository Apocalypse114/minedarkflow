package net.apocalypse.mineblackflow.block.natural;

import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BloodMushroomBlock extends SimpleNaturalObjBlock{
    public BloodMushroomBlock() {
        super(Properties.of().sound(SoundType.FUNGUS)
                .instabreak().noCollission().mapColor(MapColor.COLOR_RED)
        );
    }

    public static final VoxelShape shape = box(4, 0, 4, 12, 14, 12);

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return shape;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
        if (!EnchantmentHelper.hasSilkTouch(entity.getMainHandItem())) MBFItems.BLOOD_MUSHROOM.get().giveWithMessage(entity);
        return super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
    }
}
