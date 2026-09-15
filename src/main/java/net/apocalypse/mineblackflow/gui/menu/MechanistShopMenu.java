package net.apocalypse.mineblackflow.gui.menu;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.core.handler.AccessoryShopHandler;
import net.apocalypse.mineblackflow.entity.npc.NPCMechanist;
import net.apocalypse.mineblackflow.gui.DisplayOnlySlot;
import net.apocalypse.mineblackflow.init.MBFMenuType;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MechanistShopMenu extends AbstractContainerMenu {
    private final AccessoryShopHandler HANDLER;
    private final NPCMechanist NPC;

    private static final int slotDx = 44, slotDy = 14, invDx = 42, invDy = 134;

    public MechanistShopMenu(Inventory inventory, int id, NPCMechanist npcMechanist){
        super(MBFMenuType.MECHANIST_SHOP.get(), id);
        this.NPC = npcMechanist;
        this.HANDLER = npcMechanist == null ? null: npcMechanist.getShop();
        putSlots(inventory);
    }

    private void putSlots(Inventory inventory){
        if (HANDLER != null){
            putSeedSlot();
            for (int i = 0; i <= HANDLER.getUnlockedGoodSlot(); i++) putGoodSlot(i);
        }
        putInventory(inventory);
    }

    private void putSeedSlot(){
        addSlot(new SeedSlot(HANDLER, 0, slotDx, slotDy));
    }
    private void putGoodSlot(int index){
        int x = index % 8, y = index / 8;
        addSlot(new DisplayOnlySlot(HANDLER, index, slotDx + 26 * x, slotDy + 36 * y));
    }
    private void putInventory(Inventory inventory){
        int dx = 0, dy = 0;
        for (int i = 0; i < 36; i++){
            if (i < 9) addSlot(new Slot(inventory, i, invDx + 18 * i, invDy + 58));
            else {
                addSlot(new Slot(inventory, i, invDx + 18 * dx, invDy + 18 * dy));
                dx++;
                if (dx > 8) {dx = 0;dy++;}
            }
        }
    }

    public MechanistShopMenu(int id, Inventory inventory,  FriendlyByteBuf byteBuf){
        this(inventory, id, getNPC(byteBuf.readInt()));
        this.HANDLER.deserializeNBT(byteBuf.readNbt());
    }

    public AccessoryShopHandler getShop(){return HANDLER;}
    public NPCMechanist getNPC(){return NPC;}

    private static @Nullable NPCMechanist getNPC(int id){
        if (Minecraft.getInstance().level != null){
            Entity entity = Minecraft.getInstance().level.getEntity(id);
            if (entity instanceof NPCMechanist npcMechanist) return npcMechanist;
        }
        return null;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        if (NPC == null) return true;
        return NPC.isAlive() && pPlayer.distanceToSqr(NPC) <= 1024;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (pIndex != 0) return itemstack;
        Slot slot = this.slots.get(0);
        if (slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (!this.moveItemStackTo(itemstack1, 24, this.slots.size(), true)) {
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

    private static class SeedSlot extends SlotItemHandler{
        public SeedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
        }
    }

    public static class Provider implements MenuProvider {
        public @NotNull Component getDisplayName(){return Component.translatable("container.mine_black_flow.shop").withStyle(ChatFormatting.WHITE);}

        @javax.annotation.Nullable
        public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer){
            return new MechanistShopMenu(pPlayerInventory, pContainerId, MBFCapabilities.getData(pPlayer).getInteractingMechanist());
        }
    }
}
