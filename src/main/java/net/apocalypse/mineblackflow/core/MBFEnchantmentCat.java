package net.apocalypse.mineblackflow.core;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MBFEnchantmentCat {
    public static final EnchantmentCategory WEAPON_GENERIC = EnchantmentCategory.create("mbf_generic_weapon",
            item -> EnchantmentCategory.WEAPON.canEnchant(item)
                    || EnchantmentCategory.TRIDENT.canEnchant(item)
                    || item instanceof AxeItem);
}
