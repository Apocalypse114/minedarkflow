package net.apocalypse.mineblackflow.gui.screen;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.gui.menu.AccessoryBoxMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

public class AccessoryBoxScreen extends AbstractContainerScreen<AccessoryBoxMenu> {
    public static final ResourceLocation BG = MineBlackFlow.modLoc("textures/gui/accessory_box.png");

    public AccessoryBoxScreen(AccessoryBoxMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.imageHeight = 171;
        this.imageWidth = 194;
        this.inventoryLabelY += 18;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY){
        this.renderBackground(pGuiGraphics);
        pGuiGraphics.blit(BG, this.leftPos - 28, this.topPos + 8, 0, 0, 194, 171, 194, 187);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick){
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        for (Slot slot: this.menu.slots){
            if (!slot.isActive())
                pGuiGraphics.blit(BG, this.leftPos + slot.x, this.topPos + slot.y, 0, 171, 16, 16, 194, 187);
        }
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        pGuiGraphics.drawString(this.font, this.title, this.titleLabelX-8, this.titleLabelY+4, 0xffffff, false);
        pGuiGraphics.drawString(this.font, this.playerInventoryTitle, this.inventoryLabelX-8, this.inventoryLabelY-1, 0xffffff, false);
        int eva = this.menu.getHandler().getEvaluationTotal();
        Component evaShow = Component.translatable("container.mine_black_flow.evaluation_total").append(""+eva).withStyle(ChatFormatting.GREEN);
        int showLen = this.font.width(evaShow);
        pGuiGraphics.drawString(this.font, evaShow, this.inventoryLabelX + 153 - showLen, this.inventoryLabelY - 1, 0xffffff, false);
    }
}
