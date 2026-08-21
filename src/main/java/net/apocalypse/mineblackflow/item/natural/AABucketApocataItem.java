package net.apocalypse.mineblackflow.item.natural;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.apocalypse.mineblackflow.init.MBFDamageTypes;
import net.apocalypse.mineblackflow.item.base.AccessoryBase;
import net.apocalypse.mineblackflow.item.base.UltraApocataItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AABucketApocataItem extends AccessoryBase {
    public AABucketApocataItem() {
        super(2, "bucket_apocata", FuncCase.HOTBAR);
    }

    public static final UUID REACH_UUID = new UUID(TAG_CHANGE.hashCode(), TAG_MODIFIED.hashCode());

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack){
        ImmutableMultimap.Builder<Attribute, AttributeModifier> map = ImmutableMultimap.builder();
        map.putAll(super.getAttributeModifiers(slot, stack));
        if(slot == EquipmentSlot.MAINHAND) {
            map.put(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "apocata_modifier",
                            799324, AttributeModifier.Operation.ADDITION));
            map.put(ForgeMod.ENTITY_REACH.get(),
                    new AttributeModifier(REACH_UUID, "apocata_modifier", 32, AttributeModifier.Operation.ADDITION));
        }
        return map.build();
    }
    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack){
        return Component.translatable(this.getDescriptionId())
                .withStyle(Style.EMPTY.withColor(
                        TextColor.fromRgb(UltraApocataItems.lerpColorWithTime(
                                UltraApocataItems.APOCATA_COLOR_1, UltraApocataItems.APOCATA_COLOR_2))));
    }
    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected){
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
        if (pSlotId < 9 && pEntity instanceof LivingEntity living)
            pEntity.hurt(new DamageSource(MBFUtil.damageType(MBFDamageTypes.MANIA_SWALLOW, pLevel)),
                    (float) (living.getMaxHealth() * 0.05));
    }
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand){
        String name = pPlayer.getName().getString();
        if (name.equals("Dev") || name.equals("Apocata")){
            if (pLevel instanceof ServerLevel serverLevel){
                serverLevel.getEntitiesOfClass(Entity.class, MBFUtil.aabbOnEntity(pPlayer, 32), e -> !e.is(pPlayer))
                        .forEach(Entity::kill);
            }
            return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    public int getBaseEvaluation(){return Integer.MIN_VALUE;}
    public int getCost() {return Integer.MAX_VALUE;}
}
