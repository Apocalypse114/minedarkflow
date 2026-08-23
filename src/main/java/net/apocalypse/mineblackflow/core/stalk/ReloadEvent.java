package net.apocalypse.mineblackflow.core.stalk;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MineBlackFlow.MODID)
public class ReloadEvent {
    @SubscribeEvent
    public static void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(StalkCast.MANAGER);
    }
}
