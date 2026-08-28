package net.apocalypse.mineblackflow.event;

import net.apocalypse.mineblackflow.init.MBFKeyMappings;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientEvents {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (Minecraft.getInstance().screen == null) {
            MBFKeyMappings.OPEN_BOX.consumeClick();
        }
    }
}
