package net.apocalypse.mineblackflow.item.base;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IKeyTriggerable {
    void trigger(ItemStack stack, LivingEntity entity);
}
