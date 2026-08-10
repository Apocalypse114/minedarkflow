package net.apocalypse.mineblackflow.init;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

@SuppressWarnings("removal")
public class MBFTags {
    public static class Forge{
        public static final TagKey<EntityType<?>> BOSSES = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge:bosses"));
    }
    public static class Entities{
        public static final TagKey<EntityType<?>> MANIA_2K = TagKey.create(Registries.ENTITY_TYPE, MineBlackFlow.modLoc("mania/with_2k_limit"));
        public static final TagKey<EntityType<?>> MANIA_4K = TagKey.create(Registries.ENTITY_TYPE, MineBlackFlow.modLoc("mania/with_4k_limit"));
        public static final TagKey<EntityType<?>> MANIA_8K = TagKey.create(Registries.ENTITY_TYPE, MineBlackFlow.modLoc("mania/with_8k_limit"));
        public static final TagKey<EntityType<?>> MANIA_10K = TagKey.create(Registries.ENTITY_TYPE, MineBlackFlow.modLoc("mania/with_10k_limit"));
        public static final TagKey<EntityType<?>> MANIA_IMMUNE = TagKey.create(Registries.ENTITY_TYPE, MineBlackFlow.modLoc("mania/mania_immune"));
    }
}
