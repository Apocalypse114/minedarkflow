package net.apocalypse.mineblackflow.mobeffect;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.config.ConfigServer;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.apocalypse.mineblackflow.init.MBFDamageTypes;
import net.apocalypse.mineblackflow.init.MBFEffects;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ManiaBreakEffect extends SimpleModEffect {

    public ManiaBreakEffect() {
        super(MobEffectCategory.NEUTRAL, 0x825679);
        this.addAttributeModifier(Attributes.ATTACK_SPEED, "896091ba-ee06-31ec-8b03-663c0c4ba48b", 0.5, AttributeModifier.Operation.ADDITION);
    }

    public static final String TAG_HIT_TIME = MineBlackFlow.modTagName("MANIA_HIT_TIME");

    @Override
    public void applyEffectTick(LivingEntity pEntity, int amplifier){
        if (SimpleModEffect.applyPerSecond(pEntity.tickCount, 0)){
            doManiaDamage(pEntity, 1);
        }
        MobEffectInstance instance = pEntity.getEffect(MBFEffects.MANIA_BREAK.get());
        int leftDuration;
        if (instance != null) {
            leftDuration = instance.getDuration();
            if (leftDuration <= 300){
                double perc = leftDuration <= 1? 0: (double) leftDuration / 300;
                ManiaInjury.Tool.setManiaEP(pEntity, (float) (ManiaInjury.Tool.getManiaLimit(pEntity) * perc));
            }
        }
    }

    public static void doManiaDamage(LivingEntity pEntity, float scale){
        float amount = ConfigServer.getManiaBaseDamage()
                + ConfigServer.getManiaBoost()
                * Math.min(pEntity.getPersistentData().getInt(TAG_HIT_TIME), ConfigServer.getManiaBoostTime());
        pEntity.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.MANIA_SWALLOW, pEntity.level())), amount * scale);
    }
}
