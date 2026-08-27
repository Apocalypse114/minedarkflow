package net.apocalypse.mineblackflow.client.overlay_util;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.compat.ca.CAUtil;
import net.apocalypse.mineblackflow.config.ConfigClient;
import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class EPBarOverlay {
    public static final ResourceLocation BAR = MineBlackFlow.modLoc("textures/gui/mania_player_bar.png");

    public static final ResourceLocation BAR_SCREEN = MineBlackFlow.modLoc("textures/gui/screen_ep_bar.png");
    public static final ResourceLocation BAR_SCREEN_FRAME = MineBlackFlow.modLoc("textures/gui/screen_ep_bar_frame.png");

    private static int[] offset = new int[]{0,0};
    private static boolean offsetRead = false;

    public static void renderBar(Player living, GuiGraphics gui, int w, int h){
        float progress = ManiaInjury.Tool.getInjuryProgress(living);
        if (ConfigClient.renderInScreenStyle()){
            renderScreenBar(ManiaInjury.Tool.underManiaBreak(living), 1 - progress, gui, w, h);
        } else {
            renderDefaultBar(progress, gui, w, h);
        }
    }
    public static void renderDefaultBar(float progress, GuiGraphics gui, int w, int h){
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
    public static void renderScreenBar(boolean underBreak, float progress, GuiGraphics gui, int w, int h){
        if (progress > 0){
            if (underBreak) progress = progress * progress;
            int height = 4 + Math.round(268 * progress);
            gui.blit(BAR_SCREEN, w / 2 - 254, h / 2 + 134 - height,
                    0, 272 - height, 508,  height, 508, 276);
            if (underBreak)
                gui.blit(BAR_SCREEN_FRAME, w / 2 - 254, h / 2 - 138,
                        0, 0, 508, 276, 508, 276);
        }
    }
}
