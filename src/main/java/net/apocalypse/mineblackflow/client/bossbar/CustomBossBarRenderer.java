package net.apocalypse.mineblackflow.client.bossbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CustomBossBarRenderer {
    public ResourceLocation texture;
    public int frame_x, frame_y, bar_x, bar_y, name_x, name_y, bar_offset_x, bar_offset_y;
    public int color, name_offset_y, render_offset_y;

    public int textureWidth(){return 256;}
    public int textureHeight(){return 32;}
    public CustomBossBarRenderer(ResourceLocation texture){
        this.texture = texture;
    }
    public static @NotNull CustomBossBarRenderer of(ResourceLocation texture){
        CustomBossBarRenderer context = new CustomBossBarRenderer(texture);
        context.frame_x = 0; context.frame_y = 0; context.bar_x = 0; context.bar_y = 5;
        context.name_x = 0; context.name_y = 0;
        context.color = 0xFFFFFF;
        return context;
    }
    public CustomBossBarRenderer frame(int x, int y){
        this.frame_x = x; this.frame_y = y;
        return this;
    }
    public CustomBossBarRenderer bar(int size_x, int size_y, int offset_x, int offset_y){
        this.bar_x = size_x; this.bar_y = size_y;
        this.bar_offset_x = offset_x; this.bar_offset_y = offset_y;
        return this;
    }
    public CustomBossBarRenderer namePos(int x, int y){
        this.name_x = x; this.name_y = y;
        return this;
    }
    public CustomBossBarRenderer namePos(int y){
        return namePos(frame_x / 2, y);
    }
    public CustomBossBarRenderer offset(int mainOffset, int nameOffset){
        this.name_offset_y = nameOffset;
        this.render_offset_y = mainOffset;
        return this;
    }
    public CustomBossBarRenderer textColor(int color){
        this.color = color;
        return this;
    }

    public void doRender(GuiGraphics gui, int x, int y, float progress, Component name){
        renderCentered(gui, y, progress, name);
        customRendering(gui, x, y, progress, name);
    }

    public void render(GuiGraphics gui, int x, int y, float progress, Component name){
        int real_px = (int) (bar_x * progress);
        gui.blit(texture, x, y + render_offset_y, 0, 0,
                frame_x, frame_y, textureWidth() ,textureHeight());
        int bx = x + bar_offset_x, by = y + bar_offset_y + render_offset_y;
        gui.blit(texture, bx, by,
                0, frame_y, real_px, bar_y, textureWidth() ,textureHeight());
        gui.drawCenteredString(Minecraft.getInstance().font, name, x + name_x, y + name_y, color);
    }
    public void renderCentered(GuiGraphics gui, int y, float progress, Component name){
        render(gui, gui.guiWidth() / 2 - frame_x / 2, y, progress, name);
    }

    private void customRendering(GuiGraphics gui, int x, int y, float progress, Component name){
    }
}
