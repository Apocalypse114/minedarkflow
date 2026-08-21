package net.apocalypse.mineblackflow.particle;

import net.apocalypse.mineblackflow.core.MBFMath;
import net.apocalypse.mineblackflow.core.MBFUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

public class DogRingParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;

    public static Provider provider(SpriteSet spriteSet) {
        return new Provider(spriteSet);
    }

    public DogRingParticle(ClientLevel pLevel, double pX, double pY, double pZ, SpriteSet pSprites){
        super(pLevel, pX, pY, pZ, 0, 0, 0);
        this.spriteSet = pSprites;
        this.setSize(0.2f, 0.2f);
        this.lifetime = 10;
        this.quadSize *= 12f;
        this.gravity = 0f;
        this.hasPhysics = false;
        this.xd = 0;
        this.yd = 0;
        this.zd = 0;
        this.setSpriteFromAge(spriteSet);
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
            this.oRoll = this.roll;
            this.roll += Mth.PI * 0.2f;
            this.quadSize *= 0.85f;
        }
    }


    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet pSprites) {
            this.sprites = pSprites;
        }

        public Particle createParticle(SimpleParticleType pType, ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            return new DogRingParticle(pLevel, pX, pY, pZ, this.sprites);
        }
    }
}
