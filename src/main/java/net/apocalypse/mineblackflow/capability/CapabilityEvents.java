package net.apocalypse.mineblackflow.capability;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.data.LivingData;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.LivingDataMessage;
import net.apocalypse.mineblackflow.network.SavedDataMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber
public class CapabilityEvents {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
            event.addCapability(MineBlackFlow.modLoc("player_data"), new PlayerData.Provider());
        if (event.getObject() instanceof LivingEntity)
            event.addCapability(MineBlackFlow.modLoc("living_data"), new LivingData.Provider());
    }
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()){
            MBFCapabilities.getData(player).sendToClient(player);

            SavedData worldData = MBFCapabilities.getData(player.level());
            SavedData dimensionData = MBFCapabilities.getDimensionData(player.level());
            if (worldData != null)
                MBFNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SavedDataMessage(true, worldData));
            if (dimensionData != null)
                MBFNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SavedDataMessage(false, dimensionData));
        }
    }
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            MBFCapabilities.getData(player).sendToClient(player);
        }
    }
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            MBFCapabilities.getData(player).sendToClient(player);

            SavedData dimensionData = MBFCapabilities.getDimensionData(player.level());
            if (dimensionData != null)
                MBFNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()),
                        new SavedDataMessage(false, dimensionData));
        }
    }
    @SubscribeEvent
    public static void clonePlayer(PlayerEvent.Clone event) {
        event.getOriginal().revive();
        PlayerData original = MBFCapabilities.getData(event.getOriginal());
        PlayerData clone = MBFCapabilities.getData(event.getEntity());
        clone.syncFrom(original);
    }
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event){
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity living && !entity.level().isClientSide()){
            MBFCapabilities.getLivingData(living).sendToTracker(living);
        }
    }
    @SubscribeEvent
    public static void onTrackingStart(PlayerEvent.StartTracking event){
        if (event.getTarget() instanceof LivingEntity living  && event.getEntity() instanceof ServerPlayer serverPlayer) {
            LivingData data = MBFCapabilities.getLivingData(living);
            MBFNetwork.PACKET_HANDLER.send(
                    PacketDistributor.PLAYER.with(()-> serverPlayer), new LivingDataMessage(data, living.getId())
            );
        }
    }
}
