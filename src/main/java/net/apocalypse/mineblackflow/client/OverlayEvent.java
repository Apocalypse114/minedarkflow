package net.apocalypse.mineblackflow.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class OverlayEvent {
    @SubscribeEvent
    public static void renderOverlay(RenderGuiEvent.Post event){
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

        Player player = Minecraft.getInstance().player;

        if (player != null){
            if (player.isDeadOrDying()) return;
            GuiGraphics gui = event.getGuiGraphics();
            float halfWidth = gui.guiWidth() * 0.5f, halfHeight = gui.guiHeight() * 0.5f;
            int width = gui.guiWidth(), height = gui.guiHeight();

            NullMaskOverlay.renderNullMask(player, halfWidth, halfHeight, gui);
            if (!player.isSpectator()) EPBarOverlay.renderBar(player, gui, width, height);
        }

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
    }
}
