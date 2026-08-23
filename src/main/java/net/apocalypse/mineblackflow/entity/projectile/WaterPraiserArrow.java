package net.apocalypse.mineblackflow.entity.projectile;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.mania.ManiaInjury;
import net.apocalypse.mineblackflow.core.mania.ManiaInjurySource;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

public class WaterPraiserArrow extends SimpleProjectile{
    public WaterPraiserArrow(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public WaterPraiserArrow(EntityType<? extends AbstractArrow> pEntityType, LivingEntity shooter, Level pLevel) {
        super(pEntityType, shooter, pLevel);
    }
    public WaterPraiserArrow(PlayMessages.SpawnEntity packet, Level world){
        this(MBFEntities.WATER_PRAISER_ARROW.get(), world);
    }
    @Override
    public void doPostHurtEffects(@NotNull LivingEntity pTarget){
        super.doPostHurtEffects(pTarget);
        Entity owner = this.getOwner();
        if (owner instanceof LivingEntity living){
            double attack = living.getAttributeValue(Attributes.ATTACK_DAMAGE);
            ManiaInjury.dealManiaInjury(pTarget, (float) (attack * 25), ManiaInjurySource.fromEntity(owner, this));
            MBFUtil.playDifferedSoundAtEntity(this, MBFSounds.SHEEP_ATTACK_HIT.get(),
                    SoundSource.HOSTILE, 1, 0.1f);
        }
    }

    public static WaterPraiserArrow shootTo(LivingEntity shooter, LivingEntity target){
        WaterPraiserArrow arrow = new WaterPraiserArrow(MBFEntities.WATER_PRAISER_ARROW.get(), shooter, shooter.level());
        SimpleProjectile.shootTo(arrow, shooter, target, 1,
                shooter.getAttributeValue(Attributes.ATTACK_DAMAGE), 3,  0);
        return arrow;
    }
}
