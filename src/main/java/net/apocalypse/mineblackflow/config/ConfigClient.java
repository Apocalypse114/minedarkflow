package net.apocalypse.mineblackflow.config;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Forge's config APIs
@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = MineBlackFlow.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigClient {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    private static final ForgeConfigSpec.IntValue EP_BAR_RENDER_OFFSET_X;
    private static final ForgeConfigSpec.IntValue EP_BAR_RENDER_OFFSET_Y;


    static {
        BUILDER.push("overlay");
        EP_BAR_RENDER_OFFSET_X = BUILDER.comment("狂躁损伤条渲染x偏移。正值代表向右。范围为正负999999。")
                        .defineInRange("ep_bar_render_offset_x", 0, -999999, 999999);
        EP_BAR_RENDER_OFFSET_Y = BUILDER.comment("狂躁损伤条渲染y偏移。正值代表向下。范围为正负999999。")
                .defineInRange("ep_bar_render_offset_y", 0, -999999, 999999);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    private static boolean validateItemName(final Object obj) {
        return obj instanceof final String itemName && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(itemName));
    }

    public static int[] getEpOffset(){
        return new int[]{EP_BAR_RENDER_OFFSET_X.get(), EP_BAR_RENDER_OFFSET_Y.get()};
    }
}
