package net.apocalypse.mineblackflow.gui.menu;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.core.accessory_box.AccessoryBoxHandler;
import net.apocalypse.mineblackflow.gui.AccessoryBoxSlot;
import net.apocalypse.mineblackflow.init.MBFMenuType;
import net.minecraft.ChatFormatting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class AccessoryBoxMenu extends AbstractContainerMenu {
    private final AccessoryBoxHandler HANDLER;

    public AccessoryBoxMenu(Inventory inventory, int id){
        super(MBFMenuType.ACC_BOX.get(), id);
        HANDLER = MBFCapabilities.getData(inventory.player).getAccessoryBoxhandler();
        int dx = 0, dy = 1;
        for (int i = 0; i < HANDLER.getSlots(); i++){
            addSlot(new AccessoryBoxSlot(HANDLER, i, dx * 18, dy * 18));
            dx ++;
            if (dx > 8){
                dx = 0; dy ++;
            }
        }
        dx = 0; dy = 5;
        int cSize = inventory.getContainerSize(), armorDy = 7;
        for (int i = 9; i < cSize; i++){
            if (i < cSize - 5) {
                addSlot(new Slot(inventory, i, dx * 18, dy * 18 + 9));
                dx++;
                if (dx > 8) {dx = 0;dy++;}
            } else if (i != cSize -1){
                addSlot(new Slot(inventory, i, -22, armorDy * 18 + 9));
                armorDy--;
            }
        }
        dx = 0;
        for (int i = 0; i<9; i++){
            addSlot(new Slot(inventory, i, dx * 18, dy * 18 + 13));
            dx ++;
        }
        addSlot(new Slot(inventory, cSize-1, -22, dy * 18 + 13));
    }

    public AccessoryBoxMenu(int id, Inventory inventory,  FriendlyByteBuf byteBuf){
        this(inventory, id);
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (pIndex < 27) {
                if (!this.moveItemStackTo(itemstack1, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, 27, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    public AccessoryBoxHandler getHandler() {
        return HANDLER;
    }

    public static class Provider implements MenuProvider{
        public @NotNull Component getDisplayName(){return Component.translatable("container.mine_black_flow.accessory_box").withStyle(ChatFormatting.WHITE);}

        @Nullable
        public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer){
            return new AccessoryBoxMenu(pPlayerInventory, pContainerId);
        }
    }
}
