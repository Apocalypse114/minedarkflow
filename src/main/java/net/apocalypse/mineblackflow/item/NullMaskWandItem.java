package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.item.base.UltraApocataItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class NullMaskWandItem extends UltraApocataItems {
    public NullMaskWandItem() {
        super(new Properties().rarity(Rarity.EPIC).stacksTo(1));
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack pStack, @NotNull Player player, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (MBFUtil.nullMasked(entity)) return super.interactLivingEntity(pStack, player, entity, hand);
        if(MBFUtil.makeNullMasked(entity)) return InteractionResult.SUCCESS;
        return InteractionResult.PASS;
    }
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvance){
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvance);
        pTooltipComponents.add(Component.translatable("item.mine_black_flow.null_mask_wand.desc").withStyle(ChatFormatting.GRAY));
    }
}
