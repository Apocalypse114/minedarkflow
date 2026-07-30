package net.apocalypse.mineblackflow.config;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    static {
        BUILDER.push("mania_injury");
        MANIA_DAMAGE_LOWEST = BUILDER.comment("狂躁损伤附加伤害初始值。范围为0到1.7976931348623157e+308")
                .defineInRange("mania_damage_lowest", 0.5, 0, Double.MAX_VALUE);
        MANIA_DAMAGE_BOOST_PER_HIT = BUILDER.comment("狂躁损伤附加伤害每次。范围为0到1.7976931348623157e+308")
                .defineInRange("mania_damage_boost_per_hit", 0.25, 0, Double.MAX_VALUE);
        MANIA_DAMAGE_BOOST_TIME = BUILDER.comment("狂躁损伤附加伤害最大叠加次数。范围为0到32768")
                .defineInRange("mania_damage_boost_time", 10, 0, 32768);
        BUILDER.pop();
        BUILDER.push("sounds");
        PLAY_ANIMAL_ON_MANIA_BURST = BUILDER.comment("在狂躁损伤爆发时在客户端播放歌曲Animal，┗|｀O′|┛ 嗷~~")
                .define("play_ANIMAL_on_mania_burst", false);
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
}
