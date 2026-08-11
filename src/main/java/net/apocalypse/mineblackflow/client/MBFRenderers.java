package net.apocalypse.mineblackflow.client;

import net.apocalypse.mineblackflow.entity.renderer.ForsakenEarthshakerRenderer;
import net.apocalypse.mineblackflow.entity.renderer.SimpleProjectileRenderer;
import net.apocalypse.mineblackflow.entity.renderer.TheNullValueRenderer;
import net.apocalypse.mineblackflow.entity.renderer.WaterPraiserRenderer;
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
        event.registerEntityRenderer(MBFEntities.FORSAKEN_EARTHSHAKER.get(), ForsakenEarthshakerRenderer::new);
        event.registerEntityRenderer(MBFEntities.WATER_PRAISER.get(), WaterPraiserRenderer::new);
        event.registerEntityRenderer(MBFEntities.WATER_PRAISER_ARROW.get(), (context) ->
                new SimpleProjectileRenderer<>(context, "textures/entity/water_praiser_arrow.png"));
    }
}
