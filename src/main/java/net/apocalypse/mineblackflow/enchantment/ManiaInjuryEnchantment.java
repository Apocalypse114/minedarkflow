package net.apocalypse.mineblackflow.enchantment;

import net.apocalypse.mineblackflow.core.MBFEnchantmentCat;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;

public class ManiaInjuryEnchantment extends Enchantment {
    public ManiaInjuryEnchantment() {
        super(Rarity.UNCOMMON, MBFEnchantmentCat.WEAPON_GENERIC, EquipmentSlot.values());
    }

    @Override
    public void doPostAttack(@NotNull LivingEntity pAttacker, @NotNull Entity pTarget, int pLevel) {
        super.doPostAttack(pAttacker, pAttacker, pLevel);
        if (pTarget instanceof LivingEntity living){
            ManiaInjury.dealManiaInjury(living, 50 * pLevel, ManiaInjurySource.fromEntity(pAttacker));
        }
    }

    @Override
    public int getMaxLevel(){
        return 5;
    }
}
