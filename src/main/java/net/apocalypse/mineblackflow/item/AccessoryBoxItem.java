package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.apocalypse.mineblackflow.init.MBFKeyMappings;
import net.apocalypse.mineblackflow.item.base.CurioItemBase;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AccessoryBoxItem extends CurioItemBase {
    public AccessoryBoxItem() {
        super(new Properties(), "accessory_box");
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer,
                                                           @NotNull InteractionHand pUsedHand){
        pPlayer.openMenu(new AccessoryBoxMenu.Provider());
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced){
        pTooltipComponents.add(Component.translatable("item.mine_black_flow.accessory_box.desc1").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void appendDescText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("curios.modifiers.mbf_accessory").withStyle(ChatFormatting.GOLD));
        pTooltipComponents.add(Component.literal("  ")
                .append(Component.translatable("item.mine_black_flow.accessory_box.desc",
                                MBFKeyMappings.OPEN_BOX.getKey().getDisplayName().getString())
                        .withStyle(ChatFormatting.GRAY)));
    }
}
