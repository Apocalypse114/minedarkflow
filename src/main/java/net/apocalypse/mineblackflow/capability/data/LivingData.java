package net.apocalypse.mineblackflow.capability.data;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.capability.MBFCapabilities;
import net.apocalypse.mineblackflow.init.MBFNetwork;
import net.apocalypse.mineblackflow.network.LivingDataMessage;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class LivingData implements INBTSerializable<CompoundTag> {
    public float MANIA_EP = 0;

    public void sendToTracker(LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            MBFNetwork.PACKET_HANDLER.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                    new LivingDataMessage(this, entity.getId()));
            MineBlackFlow.LOGGER.info("living data message sent, hash: {}, value: {}", this.hashCode(), this.MANIA_EP);
        }
    }
    public static void syncFromMessage(LivingData clientData, LivingDataMessage message){
        clientData.syncFrom(message);
    }
    public void syncFrom(LivingDataMessage message){
        LivingData newData = message.get();
        syncFrom(newData);
    }
    public LivingData syncFrom(LivingData data){
        this.MANIA_EP = data.MANIA_EP;
        MineBlackFlow.LOGGER.info("receive data ,hash:{}", this.hashCode());
        return this;
    }
    public Tag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putFloat("mania_ep", MANIA_EP);
        return nbt;
    }
    public void readNBT(Tag tag) {
        CompoundTag nbt = (CompoundTag) tag;
        MANIA_EP = nbt.getFloat("mania_ep");
    }

    public CompoundTag serializeNBT(){
        return (CompoundTag) writeNBT();
    }
    public void deserializeNBT(CompoundTag nbt){
        readNBT(nbt);
    }

    @Mod.EventBusSubscriber
    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final LivingData DATA = new LivingData();
        private final LazyOptional<LivingData> INSTANCE = LazyOptional.of(() -> DATA);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == MBFCapabilities.LIVING_DATA ? INSTANCE.cast() : LazyOptional.empty();
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
