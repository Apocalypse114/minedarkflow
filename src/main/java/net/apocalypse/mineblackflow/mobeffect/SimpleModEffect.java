package net.apocalypse.mineblackflow.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class SimpleModEffect extends MobEffect {

    public SimpleModEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier){
        return true;
    }

    public static boolean applyPerSecond(int duration, int offset){
        return duration % 20 == offset;
    }
}
