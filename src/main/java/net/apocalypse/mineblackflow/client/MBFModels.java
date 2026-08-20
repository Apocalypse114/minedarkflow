package net.apocalypse.mineblackflow.client;

import net.apocalypse.mineblackflow.client.model.ProtoSignModel;
import net.apocalypse.mineblackflow.client.model.SquareProjectile;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MBFModels {
    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SquareProjectile.LAYER_LOCATION, SquareProjectile::createBodyLayer);
        event.registerLayerDefinition(ProtoSignModel.LAYER_LOCATION, ProtoSignModel::createBodyLayer);
    }
}
