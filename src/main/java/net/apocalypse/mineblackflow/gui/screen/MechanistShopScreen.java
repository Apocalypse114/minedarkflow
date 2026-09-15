package net.apocalypse.mineblackflow.gui.screen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.client.overlay_util.PixelDigit;
import net.apocalypse.mineblackflow.gui.MechanistShopButtons;
import net.apocalypse.mineblackflow.gui.menu.MechanistShopMenu;
import net.apocalypse.mineblackflow.item.base.IBlackflowiumPriced;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class MechanistShopScreen extends AbstractContainerScreen<MechanistShopMenu> {
    public static final ResourceLocation BG = MineBlackFlow.modLoc("textures/gui/mechanist_shop.png");

    public MechanistShopScreen(MechanistShopMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.imageWidth = 266;
        this.imageHeight = 216;
        this.inventoryLabelX = 41;
        this.inventoryLabelY = 125;
        this.titleLabelX = 4;
        this.titleLabelY = 3;
    }

    @Override
    public void init(){
        super.init();
        if (menu.getShop() != null) {
            addBuyButton();
            addRenderableWidget(new MechanistShopButtons.Confirm(this.leftPos + 6, this.topPos + 80, this.menu));
            addRenderableWidget(new MechanistShopButtons.Cancel(this.leftPos + 25, this.topPos + 79, this.menu));
            addRenderableWidget(new MechanistShopButtons.Refresh(this.leftPos + 24, this.topPos + 15, this.menu));
            addRenderableWidget(new MechanistShopButtons.Breed(this.leftPos + 41, this.topPos + 34, this.menu));
        }
    }
    private void addBuyButton(){
        int dx = 1, dy = 0;
        for (int i = 1; i <= this.menu.getShop().getUnlockedGoodSlot(); i++){
            addRenderableWidget(new MechanistShopButtons.Buy(this.leftPos + 41 + 26 * dx, this.topPos + 34 + 36 * dy, this.menu, i));
            dx ++;
            if (dx > 7){dx = 0; dy++;}
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY){
        this.renderBackground(pGuiGraphics);
        if (this.menu.getNPC() != null) {
            InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, this.leftPos + 18, this.topPos + 76,
                    24, this.leftPos + 18 - pMouseX, this.topPos + 45 - pMouseY, this.menu.getNPC());
        }
        pGuiGraphics.blit(BG, this.leftPos, this.topPos,
                0, 0, 266, 216, 266, 253);
        renderActiveGoodSlot(pGuiGraphics);
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick){
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        if (this.menu.getShop() != null){
            int p = this.menu.getShop().calcSelectedPrice();
            if (p > 999) p = 999;
            if (p > 0){
                PixelDigit.renderIcon(this.leftPos + 6, this.topPos + 89, pGuiGraphics);
                PixelDigit.renderInt(this.leftPos + 12, this.topPos + 89, pGuiGraphics, p, 0);
            }
            p = menu.getShop().refreshPrice;
            PixelDigit.renderIcon(this.leftPos + 12, this.topPos + 17, pGuiGraphics);
            PixelDigit.renderInt(this.leftPos + 18, this.topPos + 17, pGuiGraphics, p, 0);
        }
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics pGuiGraphics, int pX, int pY){
        super.renderTooltip(pGuiGraphics, pX, pY);
        checkPlaceAndRenderTooltip(6, 79, 17, 9, pGuiGraphics, pX, pY,
                Component.translatable("container.mine_black_flow.shop.confirm"));
        if (this.menu.getShop() != null){
            renderGoodsTooltip(pGuiGraphics, pX, pY);
        }
        checkPlaceAndRenderTooltip(6, 105, 28, 9, pGuiGraphics, pX, pY,
                Component.translatable("container.mine_black_flow.shop.rob"));
        checkPlaceAndRenderTooltip(24, 15, 9, 9, pGuiGraphics, pX, pY,
                Component.translatable("container.mine_black_flow.shop.refresh"));
        checkPlaceAndRenderTooltip(41, 34, 22, 7, pGuiGraphics, pX, pY,
                Component.translatable("container.mine_black_flow.shop.breed"));
        checkPlaceAndRenderTooltip(25, 79, 9, 9, pGuiGraphics, pX, pY,
                Component.translatable("container.mine_black_flow.shop.cancel"));
    }

    private void renderGoodsTooltip(GuiGraphics pGuiGraphics, int px, int py){
        int dx = 1, dy = 0;
        for (int i = 1; i <= this.menu.getShop().getUnlockedGoodSlot(); i++){
            checkPlaceAndRenderTooltip(41 + 26 * dx, 34 + 36 * dy, 22, 7,
                    pGuiGraphics, px, py, List.of(
                            Component.translatable("container.mine_black_flow.shop.buy_0"),
                            Component.translatable("container.mine_black_flow.shop.buy_1")
                    ));
            dx ++;
            if (dx > 7){dx = 0; dy++;}
        }
    }
    private void checkPlaceAndRenderTooltip(int px, int py, int sizeX, int sizeY,
                                            GuiGraphics pGuiGraphics, int mX, int mY, List<Component> content){
        px += this.leftPos; py += this.topPos;
        if (px <= mX && mX <= px + sizeX && py <= mY && mY <= py + sizeY)
            pGuiGraphics.renderTooltip(this.font, content, Optional.empty(), mX, mY);
    }
    private void checkPlaceAndRenderTooltip(int px, int py, int sizeX, int sizeY,
                                            GuiGraphics pGuiGraphics, int mX, int mY, Component content){
        checkPlaceAndRenderTooltip(px, py, sizeX, sizeY, pGuiGraphics, mX, mY, List.of(content));
    }

    private void renderActiveGoodSlot(GuiGraphics pGuiGraphics){
        int dx = 1, dy = 0;
        for (int i = 1; i <= this.menu.getShop().getUnlockedGoodSlot(); i++){
            pGuiGraphics.blit(BG, this.leftPos + 41 + dx * 26, this.topPos + 11 + dy * 36,
                    28, 216,
                    22, 22, 266, 253);
            if (this.menu.getShop().isIndexSelected(i)){
                pGuiGraphics.blit(BG, this.leftPos + 41 + dx * 26, this.topPos + 11 + dy * 36,
                        203, 217,
                        22, 30, 266, 253);
            }
            ItemStack stack = this.menu.getShop().getStackInSlot(i);
            if (stack.getItem() instanceof IBlackflowiumPriced p){
                PixelDigit.renderIcon(this.leftPos + 43 + dx * 26, this.topPos + 35 + dy * 36, pGuiGraphics);
                PixelDigit.renderInt(this.leftPos + 49 + dx * 26, this.topPos + 35 + dy * 36, pGuiGraphics, p.getCost(), 0);
            }
            dx ++;
            if (dx > 7){dx = 0; dy++;}
        }
    }
}
