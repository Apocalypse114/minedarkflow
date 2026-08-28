package net.apocalypse.mineblackflow.item.base;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class CurioItemBase extends Item implements ICurioItem {
    public final String descId;
    public CurioItemBase(Properties pProperties, String id) {
        super(pProperties.stacksTo(1));
        descId = "item.mine_black_flow."+id+".desc";
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        appendDescText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public void appendDescText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.translatable("curios.modifiers.mbf_accessory").withStyle(ChatFormatting.GOLD));
        pTooltipComponents.add(Component.literal("  ").append(Component.translatable(descId).withStyle(ChatFormatting.GRAY)));
    }
}
