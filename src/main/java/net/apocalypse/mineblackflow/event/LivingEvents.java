package net.apocalypse.mineblackflow.event;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.apocalypse.mineblackflow.client.overlay_util.NullMaskOverlay;
import net.apocalypse.mineblackflow.config.ConfigServer;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.apocalypse.mineblackflow.init.MBFEffects;
import net.apocalypse.mineblackflow.init.MBFTags;
import net.apocalypse.mineblackflow.mobeffect.ManiaBreakEffect;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
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
    public static void onEntityJoinLevel(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        handleManiaLimit(entity);
    }
    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event){
        LivingEntity entity = event.getEntity();
        if (entity == null) return;
        DamageSource source = event.getSource();
        Entity sourceEnt = source.getEntity();
        if (MBFUtil.isNotRealDamage(source)
                && sourceEnt instanceof LivingEntity living && living.hasEffect(MBFEffects.DOG_PROTO_BITE.get())
                && living.getRandom().nextFloat() < 0.8f){
            event.setCanceled(true);
        }
        if (event.getEntity() instanceof Player player){
            PlayerData data = MBFCapabilities.getData(player);
            data.player_lifetime_example++;
            data.sendToClient(player);
            MineBlackFlow.LOGGER.info("debug; player data:{}", MBFCapabilities.getData(player).player_lifetime_example);
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
        handleManiaHitTime(sourceEntity);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event){
        Player player = event.getEntity();
        if(player != null){
            ManiaInjury.Tool.setManiaEP(player, 0);
        }
    }

    private static void handleManiaHitTime(Entity sourceEntity){
        if (sourceEntity instanceof LivingEntity living && ManiaInjury.Tool.underManiaBreak(living)) {
            int time = living.getPersistentData().getInt(ManiaBreakEffect.TAG_HIT_TIME), limit = ConfigServer.getManiaBoostTime();
            if (time < limit) living.getPersistentData().putInt(ManiaBreakEffect.TAG_HIT_TIME, time + 1);
        }
    }
    private static void handleManiaLimit(Entity entity){
        if (entity instanceof LivingEntity living && ManiaInjury.Tool.getManiaLimitBase(living) == 1000){
            if (entity.getType().is(MBFTags.Entities.MANIA_IMMUNE)) ManiaInjury.Tool.setManiaLimitBase(living, -1);
            else if (entity.getType().is(MBFTags.Entities.MANIA_10K)) ManiaInjury.Tool.setManiaLimitBase(living, 10000);
            else if (entity.getType().is(MBFTags.Entities.MANIA_8K)) ManiaInjury.Tool.setManiaLimitBase(living, 8000);
            else if (entity.getType().is(MBFTags.Entities.MANIA_4K)) ManiaInjury.Tool.setManiaLimitBase(living, 4000);
            else if (entity.getType().is(MBFTags.Entities.MANIA_2K)) ManiaInjury.Tool.setManiaLimitBase(living, 2000);
            else if (entity.getType().is(MBFTags.Forge.BOSSES)) ManiaInjury.Tool.setManiaLimitBase(living, 4000);
            else if (living.getMobType()== MobType.UNDEAD) ManiaInjury.Tool.setManiaLimitBase(living, 3000);
            else if (living instanceof IBlackFlowMonster) ManiaInjury.Tool.setManiaLimitBase(living, 5000);
        }
    }
}
