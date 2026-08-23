package net.apocalypse.mineblackflow.capability.data;

import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.PlayerDataMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class PlayerData implements INBTSerializable<CompoundTag> {
    public double player_lifetime_example = 0;

    public void sendToClient(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            MBFNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerDataMessage(this));
    }

    public static void syncFromMessage(PlayerData clientData, PlayerDataMessage message){
        clientData.syncFrom(message);
    }

    public void syncFrom(PlayerDataMessage message){
        PlayerData newData = message.get();
        syncFrom(newData);
    }

    public PlayerData syncFrom(PlayerData data){
        this.player_lifetime_example = data.player_lifetime_example;
        return this;
    }

    public Tag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("MANIA_EP", player_lifetime_example);
        return nbt;
    }
    public void readNBT(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;
        player_lifetime_example = nbt.getDouble("MANIA_EP");
    }

    public CompoundTag serializeNBT(){
        return (CompoundTag) writeNBT();
    }
    public void deserializeNBT(CompoundTag nbt){
        readNBT(nbt);
    }

    @Mod.EventBusSubscriber
    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final PlayerData DATA = new PlayerData();
        private final LazyOptional<PlayerData> INSTANCE = LazyOptional.of(() -> DATA);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == MBFCapabilities.PLAYER_DATA ? INSTANCE.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return DATA.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            DATA.deserializeNBT(nbt);
        }
    }
}
