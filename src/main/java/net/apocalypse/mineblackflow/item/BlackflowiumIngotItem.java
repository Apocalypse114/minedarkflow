package net.apocalypse.mineblackflow.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class BlackflowiumIngotItem extends Item {
    public BlackflowiumIngotItem() {
        super(new Properties().fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
        pTooltipComponents.add(Component.translatable("item.mine_black_flow.blackflowium_ingot.desc")
                .withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}
