package net.apocalypse.mineblackflow.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class MBFButton extends ImageButton {
    @SuppressWarnings("SuspiciousNameCombination")
    public MBFButton(int pX, int pY, int pWidth, int pHeight, int textureOffsetX, int textureOffsetY,
                     ResourceLocation pResourceLocation, int textureWidth, int textureHeight,
                     OnPress onPress) {
        super(pX, pY, pWidth, pHeight,
                textureOffsetX, textureOffsetY, pWidth,
                pResourceLocation, textureWidth, textureHeight, onPress);
    }
@Override
    public void renderWidget(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int xStart = this.xTexStart;
        if (!this.isActive()) xStart += this.width * 2;
        else if (this.isHoveredOrFocused()) xStart += this.width;
        this.renderTexture(pGuiGraphics, this.resourceLocation, this.getX(), this.getY(), xStart, this.yTexStart, 0, this.width, this.height, this.textureWidth, this.textureHeight);
    }
}
