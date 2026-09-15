package net.apocalypse.mineblackflow.client.overlay_util;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class PixelDigit {
    public static final ResourceLocation loc = MineBlackFlow.modLoc("textures/gui/shop_num.png");

    public static void renderInt(int x, int y, GuiGraphics guiGraphics, int digit, int style){
        int a = digit % 10, b = digit / 10 % 10, c = digit / 100 % 10;
        if (c > 0){
            dispatchDigitRendering(x, y, guiGraphics, c, style);
            x += 4;
        }
        if (c > 0 || b > 0){
            dispatchDigitRendering(x, y, guiGraphics, b, style);
            x += 4;
        }
        if (b > 0 || a > 0){
            dispatchDigitRendering(x, y, guiGraphics, a, style);
        }
    }

    public static void renderIcon(int x, int y, GuiGraphics guiGraphics){
        guiGraphics.blit(loc, x, y, 0, 18, 5, 5, 66, 25);
    }

    public static void dispatchDigitRendering(int x, int y, GuiGraphics guiGraphics, int digit, int style){
        if (style == 0) renderSingleDigit(x, y, guiGraphics, digit);
        else if (style == 1) renderRedSingleDigit(x, y, guiGraphics, digit);
        else renderLinedSingleDigit(x, y, guiGraphics, digit);
    }

    public static void renderSingleDigit(int x, int y, GuiGraphics guiGraphics, int digit){
        guiGraphics.blit(loc, x, y, digit * 4, 0, 3, 5, 66, 25);
    }
    public static void renderRedSingleDigit(int x, int y, GuiGraphics guiGraphics, int digit){
        guiGraphics.blit(loc, x, y, digit * 4, 6, 3, 5, 66, 25);
    }
    public static void renderLinedSingleDigit(int x, int y, GuiGraphics guiGraphics, int digit){
        guiGraphics.blit(loc, x, y, digit * 4, 12, 3, 5, 66, 25);
    }
}
