package net.apocalypse.mineblackflow.event;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class PlayerEvents {
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        Player player = event.getEntity();
        if(player != null){
            ManiaInjury.Tool.setManiaEP(player, 0);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event){
        Player plr = event.player;
        if (plr == null) return;
        MBFUtil.forEachItemInAccessoryBox(plr, stack -> stack.inventoryTick(plr.level(), plr, 0, false));
    }
}
