package net.apocalypse.mineblackflow.gui;

import net.apocalypse.mineblackflow.gui.menu.MechanistShopMenu;
import net.apocalypse.mineblackflow.gui.screen.MechanistShopScreen;
import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.IntButtonMessage;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

public class MechanistShopButtons extends MBFButton{
    public static final int[] posBuy = new int[]{51, 217}, posBreed = new int[]{51, 226}, posConfirmBuy = new int[]{51, 235},
            posCancel = new int[]{51, 244}, posRobbery = new int[]{118, 217}, posSwitch = new int[]{118, 227},
            posRefresh = new int[]{79,244};

    public final MechanistShopMenu MENU;

    public MechanistShopButtons(int pX, int pY,
                                int pWidth, int pHeight,
                                int @NotNull [] pos,
                                MechanistShopMenu menu) {
        super(pX, pY, pWidth, pHeight, pos[0], pos[1], MechanistShopScreen.BG, 266, 253, pButton -> {});
        this.MENU = menu;
    }

    public static class Buy extends MechanistShopButtons{
        public final int index;
        public Buy(int pX, int pY, MechanistShopMenu menu, int index) {
            super(pX, pY, 22, 7, posBuy, menu);
            this.index = index;
        }
        @Override
        public void onPress(){
            MENU.getShop().putStackToBuy(index);
            MBFNetwork.PACKET_HANDLER.sendToServer(new IntButtonMessage.BuyMessage(index));
        }
    }
    public static class Breed extends MechanistShopButtons{
        public Breed(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 22, 7, posBreed, menu);
        }
        @Override
        public void onPress(){
            MENU.getShop().breedSeeds(Minecraft.getInstance().player);
            MBFNetwork.PACKET_HANDLER.sendToServer(new IntButtonMessage.BreedMessage());
        }
        @Override
        public boolean isActive(){
            return !this.MENU.getShop().getStackInSlot(0).isEmpty();
        }
    }
    public static class Confirm extends MechanistShopButtons{
        public Confirm(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 17, 9, posConfirmBuy, menu);
        }
        @Override
        public void onPress(){
            MENU.getShop().confirmStackToBuy(Minecraft.getInstance().player);
            MBFNetwork.PACKET_HANDLER.sendToServer(new IntButtonMessage.ConfirmMessage());
        }
        @Override
        public boolean isActive(){
            return this.MENU.getShop().isAnythingSelected();
        }
    }
    public static class Cancel extends MechanistShopButtons{
        public Cancel(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 9, 9, posCancel, menu);
        }
        @Override
        public void onPress(){
            MENU.getShop().clearToBuy();
            MBFNetwork.PACKET_HANDLER.sendToServer(new IntButtonMessage.CancelMessage());
        }
        @Override
        public boolean isActive(){
            return this.MENU.getShop().isAnythingSelected();
        }
    }
    public static class Robbery extends MechanistShopButtons{
        public Robbery(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 28, 9, posRobbery, menu);
        }
    }
    public static class Switch extends MechanistShopButtons{
        public Switch(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 11, 11, posSwitch, menu);
        }
    }
    public static class Refresh extends MechanistShopButtons{
        public Refresh(int pX, int pY, MechanistShopMenu menu) {
            super(pX, pY, 9, 9, posRefresh, menu);
        }
        @Override
        public void onPress(){
            MENU.getShop().playerRefresh(Minecraft.getInstance().player);
            MBFNetwork.PACKET_HANDLER.sendToServer(new IntButtonMessage.RefreshMessage());
        }
        @Override
        public boolean isActive(){
            return this.MENU.getShop().refreshPrice < 16;
        }
    }
}
