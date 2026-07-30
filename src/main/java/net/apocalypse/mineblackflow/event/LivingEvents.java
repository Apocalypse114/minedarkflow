package net.apocalypse.mineblackflow.event;

import net.apocalypse.mineblackflow.client.NullMaskOverlay;
import net.apocalypse.mineblackflow.config.ConfigServer;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.apocalypse.mineblackflow.mobeffect.ManiaBreakEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class LivingEvents {
    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingTickEvent event){
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        if (MBFUtil.nullMasked(entity) && !NullMaskOverlay.isEntityCollected(entity)){
            NullMaskOverlay.putMaskedEntity(entity);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event){
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        if (MBFUtil.nullMasked(entity)){
            MBFUtil.openNullMask(entity);
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event){
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        DamageSource source = event.getSource();
        Entity sourceEntity = source.getEntity();
        if (sourceEntity instanceof LivingEntity living && ManiaInjury.Tool.underManiaBreak(living)) {
            int time = living.getPersistentData().getInt(ManiaBreakEffect.TAG_HIT_TIME), limit = ConfigServer.getManiaBoostTime();
            if (time < limit) living.getPersistentData().putInt(ManiaBreakEffect.TAG_HIT_TIME, time + 1);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        Player player = event.getEntity();

        if(player != null){
            ManiaInjury.Tool.setManiaEP(player, 0);
        }
    }
}
