package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.entity.*;
import net.apocalypse.mineblackflow.entity.projectile.WaterPraiserArrow;
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
    public static final RegistryObject<EntityType<WaterPraiserEntity>> WATER_PRAISER = register("water_praiser",
            EntityType.Builder.<WaterPraiserEntity>of(WaterPraiserEntity::new, MobCategory.MONSTER)
                    .setCustomClientFactory(WaterPraiserEntity::new), 24, 0.8f, 2.2f);
    public static final RegistryObject<EntityType<WindHunterEntity>> WIND_HUNTER = register("wind_hunter",
            EntityType.Builder.<WindHunterEntity>of(WindHunterEntity::new, MobCategory.MONSTER)
                    .setCustomClientFactory(WindHunterEntity::new), 24, 0.8f, 1.8f);
    public static final RegistryObject<EntityType<HuntingDogProtoEntity>> HUNTING_DOG_PROTO = register("huntingdog_proto",
            EntityType.Builder.<HuntingDogProtoEntity>of(HuntingDogProtoEntity::new, MobCategory.MONSTER)
                    .setCustomClientFactory(HuntingDogProtoEntity::new), 28, 1.5f, 1.8f);

    public static final RegistryObject<EntityType<WaterPraiserArrow>> WATER_PRAISER_ARROW = register("water_praiser_arrow",
            EntityType.Builder.<WaterPraiserArrow>of(WaterPraiserArrow::new, MobCategory.MISC)
                    .setCustomClientFactory(WaterPraiserArrow::new), 24, 0.25f, 0.25f);


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
        event.put(WATER_PRAISER.get(), WaterPraiserEntity.createAttribute().build());
        event.put(WIND_HUNTER.get(), WindHunterEntity.createAttribute().build());
        event.put(HUNTING_DOG_PROTO.get(), HuntingDogProtoEntity.createAttribute().build());
    }
}
