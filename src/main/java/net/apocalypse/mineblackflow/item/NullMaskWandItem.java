package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

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
}
