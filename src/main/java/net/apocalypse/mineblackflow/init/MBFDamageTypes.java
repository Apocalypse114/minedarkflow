package net.apocalypse.mineblackflow.init;

import com.mojang.datafixers.util.Pair;
import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.datagen.RegistryDataProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class MBFDamageTypes {

    public static final ResourceKey<DamageType> MANIA_SWALLOW = create("mania_swallow");
    public static final ResourceKey<DamageType> BLACKFLOW_COMMON = create("blackflow_common");
    public static final ResourceKey<DamageType> BLACKFLOW_MAGIC = create("blackflow_magic");

    public static ResourceKey<DamageType> create(String name){
        return ResourceKey.create(Registries.DAMAGE_TYPE, MineBlackFlow.modLoc(name));
    }
}
