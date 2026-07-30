package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.mobeffect.ManiaBreakEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, MineBlackFlow.MODID);

    public static final RegistryObject<MobEffect> MANIA_BREAK = REGISTRY.register("mania_break",
            ()->new ManiaBreakEffect(MobEffectCategory.NEUTRAL, 0x825679));

}
