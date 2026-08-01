package net.apocalypse.mineblackflow.core;

import net.apocalypse.mineblackflow.init.MBFAttributes;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MBFUtil {
    public static boolean nullMasked(LivingEntity entity){
        if (entity == null) return false;
        if (entity instanceof Player) return false;
        AttributeInstance instance = entity.getAttribute(MBFAttributes.NULL_MASKED.get());
        if (instance != null) {
            return instance.getBaseValue() > 0.5;
        }
        return false;
    }
    public static boolean makeNullMasked(LivingEntity entity){
        if (entity == null) return false;
        if (entity instanceof Player) return false;
        AttributeInstance instance = entity.getAttribute(MBFAttributes.NULL_MASKED.get());
        if (instance != null) {
            instance.setBaseValue(1);
            return true;
        }
        return false;
    }
    public static void openNullMask(LivingEntity maskedEntity){
        AttributeInstance instance = maskedEntity.getAttribute(MBFAttributes.NULL_MASKED.get());
        if (instance != null) instance.setBaseValue(0);
        BlockPos pos = maskedEntity.blockPosition();
        Level level = maskedEntity.level();
        maskedEntity.discard();
        if (maskedEntity.isRemoved() && level instanceof ServerLevel serverLevel){
            Entity dog = MBFEntities.THE_NULL_VALUE.get().spawn(serverLevel, pos, MobSpawnType.MOB_SUMMONED);
            if (dog != null){
                dog.setYBodyRot(maskedEntity.getYRot());
                serverLevel.sendParticles(ParticleTypes.CLOUD, dog.getX(), dog.getY()+0.75, dog.getZ(), 32, 0.75, 0.75, 0.75, 0.1);
            }
        }
    }
    public static Holder<DamageType> damageType(ResourceKey<DamageType> typeKey, Level level){
        return level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(typeKey);
    }
    public static void playSoundAt(Level level, double x, double y, double z,
                                   BlockPos pos, SoundEvent event, SoundSource source,
                                   float volume, float pitch){
        if (level.isClientSide()) level.playLocalSound(x, y, z, event, source, volume, pitch, false);
        else level.playSound(null, pos, event, source, volume, pitch);
    }
    public static void playerSoundAtEntity(Entity entity, SoundEvent event, SoundSource source, float volume, float pitch){
        playSoundAt(entity.level(), entity.getX(), entity.getY(), entity.getZ(), entity.blockPosition(), event, source, volume, pitch);
    }

    public static AttributeSupplier.Builder fastBuildAttribute(
            double health, double attack, double speed, double armor, double resistance, double knockback_resist, double follow_range) {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, speed);
        builder = builder.add(Attributes.MAX_HEALTH, health);
        builder = builder.add(Attributes.ARMOR, armor);
        builder = builder.add(Attributes.ATTACK_DAMAGE, attack);
        builder = builder.add(Attributes.FOLLOW_RANGE, follow_range);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, knockback_resist);
        return builder;
    }
    public static AttributeSupplier.Builder fastBuildAttribute(
            double health, double attack, double speed, double armor, double follow_range){
        return fastBuildAttribute(health, attack, speed, armor, 0, 0, follow_range);
    }
    public static int lerpColor(int colorA, int colorB, float progress) {
        int rA = (colorA >> 16) & 0xFF;
        int gA = (colorA >> 8) & 0xFF;
        int bA = colorA & 0xFF;

        int rB = (colorB >> 16) & 0xFF;
        int gB = (colorB >> 8) & 0xFF;
        int bB = colorB & 0xFF;

        int r = (int)(rA + (rB - rA) * progress);
        int g = (int)(gA + (gB - gA) * progress);
        int b = (int)(bA + (bB - bA) * progress);

        return (r << 16) | (g << 8) | b;
    }

    public static float getPreciseTick(){
        Player player = Minecraft.getInstance().player;
        if (player==null)return 0;
        return player.tickCount + Minecraft.getInstance().getPartialTick();
    }
}
