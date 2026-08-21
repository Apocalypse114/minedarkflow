package net.apocalypse.mineblackflow.client;

import net.apocalypse.mineblackflow.init.MBFParticleTypes;
import net.apocalypse.mineblackflow.particle.DogBiteParticle;
import net.apocalypse.mineblackflow.particle.DogRingParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MBFParticles {
    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(MBFParticleTypes.BOG_BITE.get(), DogBiteParticle::provider);
        event.registerSpriteSet(MBFParticleTypes.BOG_RING.get(), DogRingParticle::provider);
    }
}
