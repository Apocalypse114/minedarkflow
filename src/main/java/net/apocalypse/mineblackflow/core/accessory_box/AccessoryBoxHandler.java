package net.apocalypse.mineblackflow.core.accessory_box;

import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class AccessoryBoxHandler extends ItemStackHandler {

    public AccessoryBoxHandler(){
        super(36);
    }

    private int validSlot = 12;
    public int getValidSlot(){
        return validSlot;
    }
    public void setValidSlot(int s){
        validSlot = s;
    }
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot <= getValidSlot() && stack.getItem() instanceof AccessoryBase;
    }
    @Override
    public int getSlotLimit(int slot)
    {
        return 16;
    }
    public void syncStacks(AccessoryBoxHandler other){
        for(int i = 0; i<this.stacks.size(); i++){
            this.stacks.set(i, other.stacks.get(i));
        }
    }

    @Override
    public CompoundTag serializeNBT(){
        CompoundTag tag = super.serializeNBT();
        tag.putInt("valid_slot", validSlot);
        return tag;
    }

    public int getEvaluationTotal(){
        int e = 0;
        for (ItemStack stack: stacks){
            e += AccessoryBase.getEvaluation(stack);
        }
        return e;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt){
        validSlot = nbt.contains("valid_slot")? nbt.getInt("valid_slot"): 12;
        super.deserializeNBT(nbt);
    }
}
