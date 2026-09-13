package net.apocalypse.mineblackflow.gui;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.resources.ResourceLocation;

public class MBFButton extends ImageButton {
    @SuppressWarnings("SuspiciousNameCombination")
    public MBFButton(int pX, int pY, int pWidth, int pHeight,
                     ResourceLocation pResourceLocation, int id,
                     OnPress onPress) {
        super(pX, pY, pWidth, pHeight,
                0, 0, pWidth,
                pResourceLocation, 3 * pWidth, pHeight, onPress);
    }
}
