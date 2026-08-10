package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.ForsakenEarthshakerEntity;
import net.apocalypse.mineblackflow.entity.TheNullValueEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MBFEntities {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(Registries.ENTITY_TYPE, MineBlackFlow.MODID);

    public static final RegistryObject<EntityType<TheNullValueEntity>> THE_NULL_VALUE = register("the_null_value",
            EntityType.Builder.<TheNullValueEntity>of(TheNullValueEntity::new, MobCategory.MONSTER)
                    .setCustomClientFactory(TheNullValueEntity::new), 22, 1, 0.8f);
    public static final RegistryObject<EntityType<ForsakenEarthshakerEntity>> FORSAKEN_EARTHSHAKER = register("forsaken_earthshaker",
            EntityType.Builder.<ForsakenEarthshakerEntity>of(ForsakenEarthshakerEntity::new, MobCategory.MONSTER)
                    .setCustomClientFactory(ForsakenEarthshakerEntity::new), 24, 3, 4);

    private static <T extends Entity> RegistryObject<EntityType<T>> register(
            String name, EntityType.Builder<T> entityTypeBuilder, int trackRange, float width, float height) {
        return REGISTRY.register(name, () -> entityTypeBuilder
                .setShouldReceiveVelocityUpdates(true)
                .setTrackingRange(trackRange).setUpdateInterval(3)
                .sized(width, height).build(name));
    }

    @SubscribeEvent
    public static void registerAttribute(EntityAttributeCreationEvent event){
        event.put(THE_NULL_VALUE.get(), TheNullValueEntity.createAttribute().build());
        event.put(FORSAKEN_EARTHSHAKER.get(), ForsakenEarthshakerEntity.createAttribute().build());
    }
}
