package net.apocalypse.mineblackflow.entity.npc;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.handler.AccessoryShopHandler;
import net.apocalypse.mineblackflow.entity.base.NotAsTarget;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PlayMessages;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Set;

public class NPCMechanist extends AbstractMechanistEntity implements NotAsTarget {
    public NPCMechanist(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }
    public NPCMechanist(PlayMessages.SpawnEntity ignored, Level world) {
        this(MBFEntities.MECHANIST_NPC.get(), world);
    }

    private final AccessoryShopHandler shop = new AccessoryShopHandler();

    public AccessoryShopHandler getShop(){return shop;}

    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    public Set<EntityType<?>> whiteList(){
        return Set.of();
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return false;
    }

    public PlayState moveHandler(AnimationState<?> event){
        if (this.isDeadOrDying()){
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        if(event.isMoving()){
            return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("move")));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }
    public PlayState attackingHandler(AnimationState<?> event) {
        return PlayState.CONTINUE;
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(40, 1, 0.2, 10, 10, 0.5, 24);
    }
}
