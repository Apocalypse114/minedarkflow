package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.mobeffect.DogProtoBiteEffect;
import net.apocalypse.mineblackflow.mobeffect.InstantManiaHealEffect;
import net.apocalypse.mineblackflow.mobeffect.InstantManiaInjuryEffect;
import net.apocalypse.mineblackflow.mobeffect.ManiaBreakEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class MBFEffects {
    public static final DeferredRegister<MobEffect> REGISTRY = DeferredRegister.create(Registries.MOB_EFFECT, MineBlackFlow.MODID);

    public static final RegistryObject<MobEffect> MANIA_BREAK = REGISTRY.register("mania_break", ManiaBreakEffect::new);
    public static final RegistryObject<MobEffect> INST_MANIA_INJURY = REGISTRY.register("instant_mania_injury", InstantManiaInjuryEffect::new);
    public static final RegistryObject<MobEffect> INST_MANIA_HEAL = REGISTRY.register("instant_mania_heal", InstantManiaHealEffect::new);

    public static final RegistryObject<MobEffect> DOG_PROTO_BITE = REGISTRY.register("dog_proto_bite", DogProtoBiteEffect::new);

}
