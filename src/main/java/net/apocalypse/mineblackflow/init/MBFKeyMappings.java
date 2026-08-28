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
    public static final KeyMapping OPEN_BOX = new OpenBox();

    private static class OpenBox extends SimpleKeyMapping<SimpleKeyMessage.OpenBoxMessage>{
        public OpenBox() {
            super("open_box", GLFW.GLFW_KEY_K, "key.categories.inventory", SimpleKeyMessage.OpenBoxMessage::new);
        }

        @Override
        public void doOnClientPress(Player player){
            if (MBFCuriosUtil.isAccessoryEquipped(player, MBFItems.ACCESSORY_BOX.get())){
                player.openMenu(new AccessoryBoxMenu.Provider());
            }
        }
    }

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        event.register(OPEN_BOX);
    }
}
