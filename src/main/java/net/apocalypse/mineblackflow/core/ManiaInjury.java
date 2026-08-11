package net.apocalypse.mineblackflow.core;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.compat.ca.CAUtil;
import net.apocalypse.mineblackflow.config.ConfigServer;
import net.apocalypse.mineblackflow.init.MBFAttributes;
import net.apocalypse.mineblackflow.init.MBFEffects;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.apocalypse.mineblackflow.mobeffect.ManiaBreakEffect;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

public class ManiaInjury {
    @SuppressWarnings("UnusedReturnValue")
    public static InjuryResult dealManiaInjury(LivingEntity living, float amount, ManiaInjurySource<?> source){
        if (Tool.immuneToMania(living)) return InjuryResult.FAIL;
        double currentEP = Tool.getManiaEP(living), newEP = currentEP, limit = Tool.getManiaLimit(living);
        amount = amountAfterCognitiveReduction(amount, living);
        if (CAUtil.checkCALoaded()) amount = amountAfterSanityResistanceReduction(amount, living);
        newEP += amount;
        if (newEP > limit){
            onManiaBreak(living);
            return InjuryResult.BREAK;
        }
        Tool.setManiaEP(living, newEP);
        return InjuryResult.SUCCESS;
    }
    public static void healManiaInjury(LivingEntity living, float amount){
        if (Tool.underManiaBreak(living)) return;
        double currentEP = Tool.getManiaEP(living), newEP = currentEP;
        newEP -= amount;
        Tool.setManiaEP(living, newEP);
    }
    public static void onManiaBreak(LivingEntity living){
        living.getPersistentData().putInt(ManiaBreakEffect.TAG_HIT_TIME, 0);
        living.addEffect(new MobEffectInstance(MBFEffects.MANIA_BREAK.get(), 300, 0, false, false));
        if (!living.hasEffect(MBFEffects.MANIA_BREAK.get())){
            Tool.setManiaEP(living, 0);
            ManiaBreakEffect.doManiaDamage(living, 2);
        }
        if (ConfigServer.playAnimal()){
            MineBlackFlow.LOGGER.info("animal played!");
            MBFUtil.playerSoundAtEntity(living, MBFSounds.ANIMAL.get(), SoundSource.NEUTRAL, 1, 1);
        }
    }
    private static float amountAfterCognitiveReduction(float amount, LivingEntity living){
        double resist = MBFAttributes.cognitiveResistance(living);
        if (resist > 0) return (float) (amount * 50 / (resist + 50));
        return amount;
    }
    public static float amountAfterSanityResistanceReduction(float amount, LivingEntity living){
        Attribute attr = CAUtil.Attributes.sanityResistance();
        if (attr == null) return amount;
        AttributeInstance instance = living.getAttribute(attr);
        if(instance == null) return amount;
        return (float) (Math.max(0, amount * (100 - instance.getValue()) * 0.01));
    }

    public static class Tool {
        public static double getManiaEP (LivingEntity living){
            AttributeInstance instance = living.getAttribute(MBFAttributes.MANIA_EP.get());
            return instance == null ? 0 : instance.getBaseValue();
        }
        public static double getManiaLimit (LivingEntity living){
            AttributeInstance instance = living.getAttribute(MBFAttributes.MANIA_LIMIT.get());
            return instance == null ? 0 : instance.getValue();
        }
        public static double getManiaLimitBase (LivingEntity living){
            AttributeInstance instance = living.getAttribute(MBFAttributes.MANIA_LIMIT.get());
            return instance == null ? 0 : instance.getBaseValue();
        }
        public static void setManiaLimitBase (LivingEntity living, double base){
            AttributeInstance instance = living.getAttribute(MBFAttributes.MANIA_LIMIT.get());
            if(instance != null) instance.setBaseValue(base);
        }
        public static void setManiaEP (LivingEntity living,double value){
            AttributeInstance instance = living.getAttribute(MBFAttributes.MANIA_EP.get());
            if (instance != null) instance.setBaseValue(Math.max(0, value));
        }
        public static float getInjuryProgress (LivingEntity living){
            return (float) (1 - getManiaEP(living) / getManiaLimit(living));
        }
        public static boolean immuneToMania (LivingEntity living){
            if (underManiaBreak(living)) return true;
            if (ConfigServer.sharedEPImmunity() &&
                    (CAUtil.checkEffect(living, CAUtil.Effects.sanityBreak())
                    || CAUtil.checkEffect(living, CAUtil.Effects.sanityImmune()))) return true;
            if (living instanceof Player player) {
                if (player.isSpectator() || player.isCreative()) return true;
            }
            return getManiaLimit(living) <= 0;
        }
        public static boolean underManiaBreak (LivingEntity living){
            return living.hasEffect(MBFEffects.MANIA_BREAK.get());
        }
    }

    public enum InjuryResult{
        FAIL, BREAK, SUCCESS;

        public boolean success(){
            return this != FAIL;
        }
    }
}
