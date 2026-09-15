package net.apocalypse.mineblackflow.entity.npc;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.core.handler.AccessoryShopHandler;
import net.apocalypse.mineblackflow.entity.base.NotAsTarget;
import net.apocalypse.mineblackflow.gui.menu.MechanistShopMenu;
import net.apocalypse.mineblackflow.init.MBFEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
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
    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (pPlayer instanceof ServerPlayer serverPlayer){
            this.shop.onOpen(pPlayer);
            PlayerData data = MBFCapabilities.getData(pPlayer);
            data.setInteractingMechanist(this);
            data.sendToClient(pPlayer);
            NetworkHooks.openScreen(serverPlayer, new MechanistShopMenu.Provider(), byteBuf -> {
                byteBuf.writeInt(this.getId());
                byteBuf.writeNbt(this.shop.serializeNBT());
            });
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void registerGoals(){
        this.goalSelector.addGoal(0, new RandomStrollGoal(this, 1));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
    }

    public Set<EntityType<?>> whiteList(){
        return Set.of();
    }

    @Override
    public void tick(){
        super.tick();
        this.shop.tickShop();
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

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag){
        super.readAdditionalSaveData(tag);
        if (tag.contains("shop")) this.shop.deserializeNBT((CompoundTag) tag.get("shop"));
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag){
        super.addAdditionalSaveData(tag);
        tag.put("shop", this.shop.serializeNBT());
    }

    public static AttributeSupplier.Builder createAttribute() {
        return MBFUtil.fastBuildAttribute(40, 1, 0.2, 10, 10, 0.5, 24);
    }
}
