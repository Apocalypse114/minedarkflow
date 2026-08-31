package net.apocalypse.mineblackflow.client;

import net.apocalypse.mineblackflow.block.base.SimpleGeoBlockEntityModel;
import net.apocalypse.mineblackflow.block.base.SimpleGeoBlockRenderer;
import net.apocalypse.mineblackflow.block.entity.RedSetariaBlockEntity;
import net.apocalypse.mineblackflow.entity.renderer.*;
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
        event.registerEntityRenderer(MBFEntities.WIND_HUNTER.get(), WindHunterRenderer::new);
        event.registerEntityRenderer(MBFEntities.HUNTING_DOG_PROTO.get(), HuntingDogProtoRenderer::new);
        event.registerEntityRenderer(MBFEntities.WATER_PRAISER_ARROW.get(), (context) ->
                new SimpleProjectileRenderer<>(context, "textures/entity/water_praiser_arrow.png"));
        event.registerEntityRenderer(MBFEntities.PROTO_SIGN.get(), ProtoSignRenderer::new);

        event.registerBlockEntityRenderer(MBFEntities.RED_SETARIA.get(),
                pContext -> new SimpleGeoBlockRenderer<>(new SimpleGeoBlockEntityModel.RedSetariaModel()));
    }
}
