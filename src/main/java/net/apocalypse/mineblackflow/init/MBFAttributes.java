package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MBFAttributes {
    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(Registries.ATTRIBUTE, MineBlackFlow.MODID);

    public static final RegistryObject<Attribute> MANIA_EP = rangedAttr("mania_value", 0, 0, Double.MAX_VALUE);
    public static final RegistryObject<Attribute> MANIA_LIMIT = rangedAttr("mania_limit", 1000, -1, Double.MAX_VALUE);

    public static final RegistryObject<Attribute> NULL_MASKED = booleanAttr("null_masked", false);

    public static RegistryObject<Attribute> rangedAttr(String name, double defaultValue, double min, double max){
        return REGISTRY.register(name, ()->new RangedAttribute("attribute."+ MineBlackFlow.MODID+"."+name, defaultValue, min, max).setSyncable(true));
    }
    public static RegistryObject<Attribute> booleanAttr(String name, boolean defaultValue){
        return rangedAttr(name, defaultValue ? 1 : 0, 0, 1);
    }

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event){

    }

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> {
            event.add(entity, MANIA_EP.get());
            event.add(entity, MANIA_LIMIT.get());
            event.add(entity, NULL_MASKED.get());
        });
    }

    @Mod.EventBusSubscriber
    public static class PlayerAttributesSync {
        public static void syncBaseValue(Player before, Player after, Attribute attribute){
            AttributeInstance ins1 = after.getAttribute(attribute);
            AttributeInstance ins2 = before.getAttribute(attribute);
            if (ins1 != null && ins2 != null) ins1.setBaseValue(ins2.getBaseValue());
        }
        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            syncBaseValue(oldPlayer, newPlayer, MANIA_EP.get());
            syncBaseValue(oldPlayer, newPlayer, MANIA_LIMIT.get());
            syncBaseValue(oldPlayer, newPlayer, NULL_MASKED.get());
        }
    }
}
