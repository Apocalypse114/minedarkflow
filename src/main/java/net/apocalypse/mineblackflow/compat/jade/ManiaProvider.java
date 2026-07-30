package net.apocalypse.mineblackflow.compat.jade;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.core.ManiaInjury;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import snownee.jade.api.EntityAccessor;
import snownee.jade.api.IEntityComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum ManiaProvider implements IEntityComponentProvider {
    INSTANCE;

    public static final ResourceLocation UID = MineBlackFlow.modLoc("mania_provider");

    @Override
    public void appendTooltip(ITooltip iTooltip, EntityAccessor entityAccessor, IPluginConfig iPluginConfig) {
        Entity entity = entityAccessor.getEntity();
        double ep;
        if (entity instanceof LivingEntity living) {
            iTooltip.add(new ManiaElement(getInjury(living), getMax(living)));
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    private double getInjury(LivingEntity entity) {
        return ManiaInjury.Tool.getManiaEP(entity);
    }

    private double getMax(LivingEntity entity) {
        return ManiaInjury.Tool.getManiaLimit(entity);
    }
}
