package net.apocalypse.mineblackflow.client.bossbar;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Mod.EventBusSubscriber
public class CustomBossBarHandler {
    private static final Map<UUID, CustomBossBarRenderer> CUSTOM_BOSS_BAR_ID = new HashMap<>();

    public static final CustomBossBarRenderer TEST_RENDERER = CustomBossBarRenderer
            .of(barLoc("test")).frame(182,5).bar(180,3,1,1).namePos(5).offset(0, 0);

    public static void put( @NotNull BossEvent bossEvent, CustomBossBarRenderer renderer){
        CUSTOM_BOSS_BAR_ID.put(bossEvent.getId(), renderer);
    }
    public static @Nullable CustomBossBarRenderer get(@NotNull BossEvent bossEvent){
        return CUSTOM_BOSS_BAR_ID.get(bossEvent.getId());
    }
    private static ResourceLocation barLoc(String name){
        return MineBlackFlow.modLoc("textures/gui/bossbar/"+name+".png");
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void customBossBar(CustomizeGuiOverlayEvent.BossEventProgress event){
        if (event.isCanceled()) return;
        BossEvent bossEvent = event.getBossEvent();
        CustomBossBarRenderer bossBarRenderer = get(bossEvent);
        if (bossBarRenderer != null){
            bossBarRenderer.doRender(event.getGuiGraphics(), event.getX(), event.getY(), bossEvent.getProgress(), bossEvent.getName());
            event.setCanceled(true);
        }
    }
}
