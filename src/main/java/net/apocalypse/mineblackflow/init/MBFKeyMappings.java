package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.compat.curios.MBFCuriosUtil;
import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.apocalypse.mineblackflow.network.SimpleKeyMessage;
import net.apocalypse.mineblackflow.network.key.SimpleKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = {Dist.CLIENT})
public class MBFKeyMappings {
    public static final KeyMapping OPEN_BOX = new TriggerAccessory();

    private static class TriggerAccessory extends SimpleKeyMapping<SimpleKeyMessage.TriggerAccessoryMessage>{
        public TriggerAccessory() {
            super("trigger_accessory", GLFW.GLFW_KEY_K, "key.categories.inventory", SimpleKeyMessage.TriggerAccessoryMessage::new);
        }

        @Override
        public void doOnClientRelease(Player player, int dms){
            if (dms > 1000) MBFCuriosUtil.triggerAccessory(player, 1);
            else MBFCuriosUtil.triggerAccessory(player, 0);
        }
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_BOX);
    }
}
