package net.apocalypse.mineblackflow.core.stalk;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraftforge.common.IExtensibleEnum;

public enum StalkResource implements IExtensibleEnum {
    TEST_ANIMAL("blackflow_animals"),
    TEST_ZOMBIE("many_zombie")
    ;

    private final ResourceLocation location;
    StalkResource(String id){
        this.location = MineBlackFlow.modLoc(id);
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public static StalkResource random(RandomSource source){
        return values()[Mth.nextInt(source, 0, values().length - 1)];
    }

    public static StalkResource create(String name, String id)
    {
        throw new IllegalStateException("Enum not extended");
    }
}
