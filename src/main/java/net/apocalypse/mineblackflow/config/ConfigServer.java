package net.apocalypse.mineblackflow.config;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = MineBlackFlow.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigServer {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.DoubleValue MANIA_DAMAGE_LOWEST;
    private static final ForgeConfigSpec.DoubleValue MANIA_DAMAGE_BOOST_PER_HIT;
    private static final ForgeConfigSpec.IntValue MANIA_DAMAGE_BOOST_TIME;
    private static final ForgeConfigSpec.BooleanValue PLAY_ANIMAL_ON_MANIA_BURST;
    private static final ForgeConfigSpec.BooleanValue MANIA_INJURY_DURING_SANITY_BREAK;

    static {
        BUILDER.push("mania_injury");
        MANIA_DAMAGE_LOWEST = BUILDER.comment("狂躁损伤附加伤害初始值。")
                .defineInRange("mania_damage_lowest", 0.5, 0, Double.MAX_VALUE);
        MANIA_DAMAGE_BOOST_PER_HIT = BUILDER.comment("狂躁损伤附加伤害每次。")
                .defineInRange("mania_damage_boost_per_hit", 0.25, 0, Double.MAX_VALUE);
        MANIA_DAMAGE_BOOST_TIME = BUILDER.comment("狂躁损伤附加伤害最大叠加次数。")
                .defineInRange("mania_damage_boost_time", 10, 0, 32768);
        BUILDER.pop();
        BUILDER.push("sounds");
        PLAY_ANIMAL_ON_MANIA_BURST = BUILDER.comment("在狂躁损伤爆发时在客户端播放歌曲Animal，┗|｀O′|┛ 嗷~~")
                .define("play_ANIMAL_on_mania_burst", false);
        BUILDER.pop();
        BUILDER.push("compat");
        MANIA_INJURY_DURING_SANITY_BREAK = BUILDER.comment("神经损伤爆发期间能造成狂躁损伤")
                .define("mania_during_sanity", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    public static float getManiaBaseDamage(){
        return MANIA_DAMAGE_LOWEST.get().floatValue();
    }
    public static float getManiaBoost(){
        return MANIA_DAMAGE_BOOST_PER_HIT.get().floatValue();
    }
    public static int getManiaBoostTime(){
        return MANIA_DAMAGE_BOOST_TIME.get();
    }
    public static boolean playAnimal(){
        return PLAY_ANIMAL_ON_MANIA_BURST.get();
    }
    public static boolean sharedEPImmunity(){
        return !MANIA_INJURY_DURING_SANITY_BREAK.get();
    }
}
