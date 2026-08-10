package net.apocalypse.mineblackflow.client.overlay_util;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.compat.ca.CAUtil;
import net.apocalypse.mineblackflow.config.ConfigClient;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.ModList;

public class EPBarOverlay {
    public static final ResourceLocation BAR = MineBlackFlow.modLoc("textures/gui/mania_player_bar.png");

    private static int[] offset = new int[]{0,0};
    private static boolean offsetRead = false;

    public static void renderBar(Player living, GuiGraphics gui, int w, int h){
        float progress = ManiaInjury.Tool.getInjuryProgress(living);
        if (progress < 1) {
            if (!offsetRead) {
                offsetRead = true;
                offset = ConfigClient.getEpOffset();
            }
            int dx = offset[0], dy = offset[1];
            if (CAUtil.checkCALoaded()) dy -= 8;
            gui.blit(BAR, w / 2 + 93 + dx, h - 12 + dy,
                    0, 4, 62, 8, 62, 12);
            gui.blit(BAR, w / 2 + 93 + dx + 10, h - 12 + dy + 3,
                    0, 0, (int) (50 * progress), 4, 62, 12);
        }
    }
}
