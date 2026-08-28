package net.apocalypse.mineblackflow.network.key;

import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.SimpleKeyMessage;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.function.BiFunction;

public class SimpleKeyMapping<M extends SimpleKeyMessage> extends KeyMapping {
    private final BiFunction<Integer, Integer, M> factory;
    public SimpleKeyMapping(String pName, int pKeyCode, String pCategory, BiFunction<Integer, Integer, M> messageFactory) {
        super("key.mine_black_flow."+pName, pKeyCode, pCategory);
        factory = messageFactory;
    }
    public void doOnClientPress(Player player){

    }
    public void doOnClientRelease(Player player, int msInPress){

    }

    private boolean isDownO = false;

    @Override
    public void setDown(boolean isDown) {
        super.setDown(isDown);
        if (isDown != isDownO) {
            if (isDown) {
                MBFNetwork.PACKET_HANDLER.sendToServer(factory.apply(0, 0));
                doOnClientPress(Minecraft.getInstance().player);
                lastPress = System.currentTimeMillis();
            } else {
                int dt = (int) (System.currentTimeMillis() - lastPress);
                MBFNetwork.PACKET_HANDLER.sendToServer(factory.apply(1, dt));
                doOnClientRelease(Minecraft.getInstance().player, dt);
            }
        }
        isDownO = isDown;
    }

    private long lastPress = 0;
}
