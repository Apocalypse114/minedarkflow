package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class MBFParticleTypes {
    public static final DeferredRegister<ParticleType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, MineBlackFlow.MODID);

    public static final RegistryObject<SimpleParticleType> BOG_BITE = simple("dog_bite");
    public static final RegistryObject<SimpleParticleType> BOG_RING = simple("dog_ring");

    public static RegistryObject<SimpleParticleType> simple(String name){
        return REGISTRY.register(name, () -> new SimpleParticleType(false));
    }
}
