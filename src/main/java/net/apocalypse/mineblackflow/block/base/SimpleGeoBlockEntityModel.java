package net.apocalypse.mineblackflow.block.base;

import net.apocalypse.mineblackflow.MineBlackFlow;
import net.apocalypse.mineblackflow.block.RedSetariaPlant;
import net.apocalypse.mineblackflow.block.entity.RedSetariaBlockEntity;
import net.apocalypse.mineblackflow.block.entity.SimpleGeoBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SimpleGeoBlockEntityModel<T extends SimpleGeoBlockEntity> extends GeoModel<T> {
    public SimpleGeoBlockEntityModel(String anim, String model, String texture){
        this.ANIM = MineBlackFlow.modLoc("animations/block/" + anim + ".animation.json");
        this.MODEL = MineBlackFlow.modLoc("geo/block/" + model + ".geo.json");;
        this.TEXTURE = MineBlackFlow.modLoc("textures/block/" + texture + ".png");
    }
    public SimpleGeoBlockEntityModel(String model, String texture){
        this("empty", model, texture);
    }
    public SimpleGeoBlockEntityModel(String res_id){
        this(res_id, res_id);
    }

    public final ResourceLocation ANIM;
    public final ResourceLocation MODEL;
    public final ResourceLocation TEXTURE;

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return ANIM;
    }

    @Override
    public ResourceLocation getModelResource(T entity) {
        return MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return TEXTURE;
    }

    public static class RedSetariaModel extends SimpleGeoBlockEntityModel<RedSetariaBlockEntity>{
        public final ResourceLocation MODEL_1, MODEL_2, MODEL_3, MODEL_4;

        public RedSetariaModel() {
            super("red_setaria");
            MODEL_1 = MineBlackFlow.modLoc("geo/block/red_setaria_1.geo.json");
            MODEL_2 = MineBlackFlow.modLoc("geo/block/red_setaria_2.geo.json");
            MODEL_3 = MineBlackFlow.modLoc("geo/block/red_setaria_3.geo.json");
            MODEL_4 = MineBlackFlow.modLoc("geo/block/red_setaria_4.geo.json");
        }

        @Override
        public ResourceLocation getModelResource(RedSetariaBlockEntity entity) {
            int count = entity.getBlockState().getValue(RedSetariaPlant.COUNT);
            return switch (count){
                case 2 -> MODEL_2;
                case 3 -> MODEL_3;
                case 4 -> MODEL_4;
                default -> MODEL_1;
            };
        }
    }
}
