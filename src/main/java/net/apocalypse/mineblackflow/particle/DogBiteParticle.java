package net.apocalypse.mineblackflow.particle;

import net.apocalypse.mineblackflow.core.MBFMath;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DogBiteParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public static Provider provider(SpriteSet spriteSet) {
        return new Provider(spriteSet);
    }

    public static int COLOR_0 = 0xa20b4b, COLOR_1 = 0xa2160b;

    public DogBiteParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprites){
        super(pLevel, pX, pY, pZ, 0, 0, 0);
        this.spriteSet = pSprites;
        this.setSize(0.2f, 0.2f);
        this.lifetime = 7;
        this.quadSize *= 6f;
        this.gravity = 0f;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setSpriteFromAge(spriteSet);
        float[] rgb = MBFMath.splitRGB(COLOR_0);
        this.setColor(rgb[0], rgb[1], rgb[2]);
    }
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
    public int getLightColor(float pPartialTick) {
        return 15728880;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSprite(this.spriteSet.get(this.age % 8 + 1, 8));
            int c = MBFUtil.lerpColor(COLOR_0, COLOR_1, (float) age / 7);
            float[] rgb = MBFMath.splitRGB(c);
            this.setColor(rgb[0], rgb[1], rgb[2]);
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DogBiteParticle(pLevel, pX, pY, pZ, this.sprites);
        }
    }
}
