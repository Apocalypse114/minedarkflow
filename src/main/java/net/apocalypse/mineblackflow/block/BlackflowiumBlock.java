package net.apocalypse.mineblackflow.block;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.List;

public class BlackflowiumBlock extends Block {
    public BlackflowiumBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.AMETHYST)
                .strength(1.5f, 16)
                .requiresCorrectToolForDrops()
                .mapColor(MapColor.COLOR_CYAN)
        );
    }

    @Override
    public void appendHoverText(ItemStack itemstack, BlockGetter level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("block.mine_black_flow.blackflowium.value").append("64")
                .withStyle(ChatFormatting.GREEN));
    }
}
