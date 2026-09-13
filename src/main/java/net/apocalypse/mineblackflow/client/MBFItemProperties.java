package net.apocalypse.mineblackflow.client;


import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.init.MBFItems;
import net.apocalypse.mineblackflow.item.base.MultiStateSpawnEgg;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MBFItemProperties {
    @SuppressWarnings("deprecation")
    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(MBFItems.MECHANIST_SPAWN_EGG.get(),
                MineBlackFlow.modLoc("mechanist_type"), MultiStateSpawnEgg::getItemType));
    }
}
