package net.apocalypse.mineblackflow.entity.projectile;

import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.apocalypse.mineblackflow.core.ManiaInjurySource;
import net.apocalypse.mineblackflow.entity.base.GeoBlackFlowMonster;
import net.apocalypse.mineblackflow.entity.base.IBlackFlowMonster;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.NotNull;

public class SimpleProjectile extends AbstractArrow {
    public SimpleProjectile(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public SimpleProjectile(EntityType<? extends AbstractArrow> pEntityType, LivingEntity shooter, Level pLevel) {
        super(pEntityType, shooter, pLevel);
    }

    public @NotNull ItemStack getPickupItem(){
        return ItemStack.EMPTY;
    }

    @Override
    public void tick(){
        super.tick();
        if (this.inGround) this.discard();
    }
    @Override
    public void onHitEntity(@NotNull EntityHitResult pResult){
        Entity entity = pResult.getEntity(), owner = this.getOwner();
        if (owner instanceof Mob mob && entity instanceof LivingEntity living && !IBlackFlowMonster.isValidTarget(mob, living)) return;
        super.onHitEntity(pResult);
    }
    @Override
    public void doPostHurtEffects(@NotNull LivingEntity pTarget){
        super.doPostHurtEffects(pTarget);
        pTarget.invulnerableTime = 0;
    }

    public static void shootTo(SimpleProjectile entityArrow, LivingEntity shooter, LivingEntity target,
                               float v, double damage, float inaccuracy,  double heightOffset) {
        double dx = target.getX() - shooter.getX();
        double dy = target.getY() + target.getEyeHeight() - heightOffset;
        double dz = target.getZ() - shooter.getZ();
        entityArrow.shoot(dx, dy - entityArrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, v, inaccuracy);
        entityArrow.setSilent(true);
        entityArrow.setBaseDamage(damage);
        entityArrow.setKnockback(0);
        entityArrow.setCritArrow(false);
        shooter.level().addFreshEntity(entityArrow);
        //shooter.level().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),shootSound, SoundSource.HOSTILE, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
    }

}
