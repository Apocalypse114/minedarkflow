package net.apocalypse.mineblackflow.client;

import net.apocalypse.mineblackflow.entity.renderer.TheNullValueRenderer;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MBFRenderers {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event){
        event.registerEntityRenderer(MBFEntities.THE_NULL_VALUE.get(), TheNullValueRenderer::new);
    }
}
