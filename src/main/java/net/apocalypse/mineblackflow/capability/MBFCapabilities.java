package net.apocalypse.mineblackflow.capability;

import net.apocalypse.mineblackflow.capability.data.DimensionData;
import net.apocalypse.mineblackflow.capability.data.LivingData;
import net.apocalypse.mineblackflow.capability.data.PlayerData;
import net.apocalypse.mineblackflow.capability.data.WorldData;
import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.LivingDataMessage;
import net.apocalypse.mineblackflow.network.PlayerDataMessage;
import net.apocalypse.mineblackflow.network.SavedDataMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MBFCapabilities {
    public static final Capability<PlayerData> PLAYER_DATA = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<LivingData> LIVING_DATA = CapabilityManager.get(new CapabilityToken<>(){});

    public static PlayerData getData(@NotNull Player player){
        return player.getCapability(PLAYER_DATA).orElseGet(PlayerData::new);
    }
    public static WorldData getData(Level pLevel){
        return WorldData.get(pLevel);
    }
    public static DimensionData getDimensionData(Level pLevel){
        return DimensionData.get(pLevel);
    }
    public static LivingData getLivingData(LivingEntity entity){
        return entity.getCapability(LIVING_DATA).orElseGet(LivingData::new);
    }

    public static @Nullable PlayerData getClientPlayerData(){
        if (Minecraft.getInstance().player != null) {
            return getData(Minecraft.getInstance().player);
        }
        return null;
    }

    @SubscribeEvent
    public static void setupNetwork(FMLCommonSetupEvent event){
        MBFNetwork.addNetworkMessage(SavedDataMessage.class, SavedDataMessage::buffer,
                SavedDataMessage::new, SavedDataMessage::handler);
        MBFNetwork.addNetworkMessage(PlayerDataMessage.class, PlayerDataMessage::buffer,
                PlayerDataMessage::new, PlayerDataMessage::handler);
        MBFNetwork.addNetworkMessage(LivingDataMessage.class, LivingDataMessage::buffer,
                LivingDataMessage::new, LivingDataMessage::handler);
    }
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event) {
        event.register(LivingData.class);
        event.register(PlayerData.class);
    }
}
