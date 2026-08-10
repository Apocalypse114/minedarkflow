package net.apocalypse.mineblackflow.mobeffect;

import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InstantManiaHealEffect extends MobEffect {
    public InstantManiaHealEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xd6a8cc);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(
            @Nullable Entity pSource, @Nullable Entity pIndirect, @NotNull LivingEntity pEntity, int amplifier, double health){
        ManiaInjury.healManiaInjury(pEntity, amplifier * 125 + 125);
    }

    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier){
        ManiaInjury.healManiaInjury(pLivingEntity, pAmplifier * 125 + 125);
    }
}
