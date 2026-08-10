package net.apocalypse.mineblackflow.client.overlay_util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;

public class NullMaskOverlay {
    private static final Set<LivingEntity> maskedEntities = new HashSet<>();
    private static final float modifier = 1, scaler = 256;
    private static final double maxDist = 32 * 32;
    private static final Vec3 elementalY = new Vec3(0, 1,0);

    private static final ResourceLocation NULL_MASK = MineBlackFlow.modLoc("textures/gui/null_mask.png");
    private static final ResourceLocation NULL_MASK_1 = MineBlackFlow.modLoc("textures/gui/null_mask_1.png");

    private static final int[] maskOrangeOffset = new int[]{0,0};
    private static int lastOffsetUpdateTick = 0;

    public static void renderNullMask(Player player, float halfWidth, float halfHeight, GuiGraphics gui){
        Set<LivingEntity> copy = new HashSet<>(maskedEntities);
        for (LivingEntity entity: copy){
            if (entity == null || entity.isRemoved()){
                maskedEntities.remove(entity);
                continue;
            }
            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            double dist = camera.getPosition().distanceToSqr(entity.position());
            if (dist > maxDist) return;
            Vec2 offset = getScreenOffset(player, entity, camera);
            if (offset.x > 999) return;
            Vec2 size = getMaskSize(Math.sqrt(dist), entity);
            int px = (int) (halfWidth - halfWidth * offset.x - size.x * 0.5f), py = (int) (halfHeight - halfHeight * offset.y - size.y);
            float alpha = 1;
            if (dist < 256){
                alpha = Math.max(0.33f, (float) (dist * 0.00390625));
            }
            RenderSystem.setShaderColor(1, 1, 1, alpha);
            gui.blit(NULL_MASK, px, py, px, py, (int)size.x, (int)size.y, 256, 256);
            gui.blit(NULL_MASK_1, px, py, maskOrangeOffset[0], maskOrangeOffset[1], (int)size.x, (int)size.y, 256, 256);
            RenderSystem.setShaderColor(1, 1, 1, 1);

            if (player.tickCount != lastOffsetUpdateTick) {
                lastOffsetUpdateTick = player.tickCount;
                maskOrangeOffset[0] = Mth.nextInt(player.getRandom(), 0, 196);
                maskOrangeOffset[1] = Mth.nextInt(player.getRandom(), 0, 196);
            }

        }
    }

    public static boolean isEntityCollected(@NotNull LivingEntity entity) {
        return maskedEntities.contains(entity);
    }
    public static void putMaskedEntity(@NotNull LivingEntity entity){
        maskedEntities.add(entity);
    }
    public static Vec2 getScreenOffset(@NotNull Player player, @NotNull LivingEntity living, @NotNull Camera camera){
        Vector3f cameraLook = camera.getLookVector();
        Vec3 a = new Vec3(cameraLook.x, cameraLook.y, cameraLook.z), cameraPos = camera.getPosition();

        Vec3 b = cameraPos.vectorTo(living.position());

        double dotProduct = a.dot(b);
        if (dotProduct < 1e-6) return Vec2.MAX;
        Vec3 c = b.add(a.reverse().scale(dotProduct)).scale(1 / dotProduct);

        Vec3 elementalX = new Vec3(a.z, 0, -a.x);

        float dx = (float) c.dot(elementalX), dy = (float) c.dot(elementalY);
        return new Vec2(dx * 0.8f, dy * 1.5f).scale(modifier);
    }
    public static Vec2 getMaskSize(double dist, LivingEntity living){
        float w = living.getBbWidth(), h = living.getBbHeight();

        dist = Math.max(dist, 1);
        return new Vec2(w, h * 0.75f).scale((float) (scaler / dist));
    }
}
