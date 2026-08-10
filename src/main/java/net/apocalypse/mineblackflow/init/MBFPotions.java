package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MBFPotions {
    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, MineBlackFlow.MODID);

    public static final RegistryObject<Potion> INST_MANIA_INJURY = REGISTRY.register("mania_injury", () ->
            new Potion(new MobEffectInstance(MBFEffects.INST_MANIA_INJURY.get(), 1, 0, false, true)));
    public static final RegistryObject<Potion> INST_MANIA_INJURY_STRENGTHENED = REGISTRY.register("mania_injury_strengthened", () ->
            new Potion(new MobEffectInstance(MBFEffects.INST_MANIA_INJURY.get(), 1, 1, false, true)));
    public static final RegistryObject<Potion> INST_MANIA_HEAL = REGISTRY.register("mania_heal", () ->
            new Potion(new MobEffectInstance(MBFEffects.INST_MANIA_HEAL.get(), 1, 0, false, true)));
    public static final RegistryObject<Potion> INST_MANIA_HEAL_STRENGTHENED = REGISTRY.register("mania_heal_strengthened", () ->
            new Potion(new MobEffectInstance(MBFEffects.INST_MANIA_HEAL.get(), 1, 1, false, true)));
}
