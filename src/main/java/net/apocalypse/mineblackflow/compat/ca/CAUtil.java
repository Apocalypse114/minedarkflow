package net.apocalypse.mineblackflow.compat.ca;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("removal")
public class CAUtil {
    private static boolean CALoaded = false, CALoadChecked = false;
    private static MobEffect sanity_break = null, sanity_immune = null;

    public static boolean checkEffect(LivingEntity living, @Nullable MobEffect effect){
        if (effect == null) return false;
        return living.hasEffect(effect);
    }

    public static class Effects{
        @Nullable
        public static MobEffect sanityBreak(){
            if (checkCALoaded()) {
                if (sanity_break == null) {
                    sanity_break = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("caerula_arbor:under_break"));
                }
                return sanity_break;
            }
            return null;
        }
        @Nullable
        public static MobEffect sanityImmune(){
            if (checkCALoaded()) {
                if (sanity_immune == null) {
                    sanity_immune = ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("caerula_arbor:sanity_immue"));
                }
                return sanity_immune;
            }
            return null;
        }
    }

    public static boolean checkCALoaded(){
        if (!CALoadChecked){
            CALoaded = ModList.get().isLoaded("caerula_arbor");
            CALoadChecked = true;
        }
        return CALoaded;
    }
}
