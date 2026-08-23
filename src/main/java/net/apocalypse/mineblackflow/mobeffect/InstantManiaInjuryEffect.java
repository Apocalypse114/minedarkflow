package net.apocalypse.mineblackflow.mobeffect;

import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.apocalypse.mineblackflow.core.mania.ManiaInjurySource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class InstantManiaInjuryEffect extends MobEffect {
    public InstantManiaInjuryEffect() {
        super(MobEffectCategory.HARMFUL, 0x411b36);
    }

    @Override
    public boolean isInstantenous() {
        return true;
    }

    @Override
    public void applyInstantenousEffect(
            @Nullable Entity pSource, @Nullable Entity pIndirect, @NotNull LivingEntity pEntity, int amplifier, double health){
        ManiaInjury.dealManiaInjury(pEntity, amplifier * 125 + 125, ManiaInjurySource.fromEntity(pIndirect, pSource));
    }

    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier){
        ManiaInjury.dealManiaInjury(pLivingEntity, pAmplifier * 125 + 125, ManiaInjurySource.fromEffect(this));
    }
}
