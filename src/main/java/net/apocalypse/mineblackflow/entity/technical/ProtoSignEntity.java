package net.apocalypse.mineblackflow.entity.technical;

import net.apocalypse.mineblackflow.entity.HuntingDogProtoEntity;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.apocalypse.mineblackflow.init.MBFParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class ProtoSignEntity extends Entity {
    public final AnimationState ANIMATION_STATE = new AnimationState();
    public HuntingDogProtoEntity owner = null;
    public UUID ownerUUID = null;

    public ProtoSignEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public ProtoSignEntity(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.PROTO_SIGN.get(), world);
    }

    public static void setup(HuntingDogProtoEntity owner){
        if (owner.level() instanceof ServerLevel serverLevel) {
            ProtoSignEntity sign = MBFEntities.PROTO_SIGN.get().create(serverLevel);
            if (sign != null) {
                sign.owner = owner;
                sign.setPos(owner.position());
                serverLevel.addFreshEntity(sign);
                serverLevel.sendParticles(MBFParticleTypes.BOG_RING.get(),
                        sign.getX(), sign.getY() + 1.5, sign.getZ(), 1,
                        0, 0, 0, 0);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(Vec3.ZERO);
        this.noPhysics = true;
        this.setNoGravity(true);
        if (this.tickCount > 211) this.discard();
        if (owner != null){
            if (owner.inSkill()) this.setPos(owner.posPrevious());
            else this.discard();
        } else if (ownerUUID != null && this.level() instanceof ServerLevel serverLevel){
            Entity entity = serverLevel.getEntity(ownerUUID);
            if (entity instanceof HuntingDogProtoEntity dog) owner = dog;
        }
        if (this.level().isClientSide()){
            this.ANIMATION_STATE.animateWhen(true, this.tickCount);
        }
    }

    @Override
    public boolean fireImmune(){
        return true;
    }

    public void defineSynchedData(){
    }
    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
    }
}
