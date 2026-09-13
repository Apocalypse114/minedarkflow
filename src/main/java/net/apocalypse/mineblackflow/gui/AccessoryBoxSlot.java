package net.apocalypse.mineblackflow.gui;

import net.apocalypse.mineblackflow.core.handler.AccessoryBoxHandler;
import net.minecraftforge.items.SlotItemHandler;

public class AccessoryBoxSlot extends SlotItemHandler {
    private final AccessoryBoxHandler accessoryBoxHandler;

    public AccessoryBoxSlot(AccessoryBoxHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        accessoryBoxHandler = itemHandler;
    }

    public AccessoryBoxHandler getBoxHandler() {
        return accessoryBoxHandler;
    }

    @Override
    public boolean isActive() {
        return !accessoryBoxHandler.getStackInSlot(this.getSlotIndex()).isEmpty() || getSlotIndex() < accessoryBoxHandler.getValidSlot();
    }
}
