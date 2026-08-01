package net.apocalypse.mineblackflow.item;

import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class UltraApocataItems extends Item {
    public UltraApocataItems(Properties p_41383_) {
        super(p_41383_);
    }

    public static final int APOCATA_COLOR_1 = 0xffcecc, APOCATA_COLOR_2 = 0xcc7eaa;

    @Override
    public @NotNull Component getName(@NotNull ItemStack pStack){
        return Component.translatable(this.getDescriptionId())
                .withStyle(Style.EMPTY.withColor(TextColor.fromRgb(lerpColorWithTime(APOCATA_COLOR_1, APOCATA_COLOR_2))));
    }

    public static int lerpColorWithTime(int colorA, int colorB){
        float p = (MBFUtil.getPreciseTick() % 80) / 40;
        return MBFUtil.lerpColor(colorA, colorB, p <= 1f ? p: 2 - p);
    }
}
