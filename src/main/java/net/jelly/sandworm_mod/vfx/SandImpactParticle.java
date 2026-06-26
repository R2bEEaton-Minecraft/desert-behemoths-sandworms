package net.jelly.sandworm_mod.vfx;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

public class SandImpactParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected SandImpactParticle(ClientLevel level, double x, double y, double z,
                                 double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.gravity = 0.01f;
        this.friction = 0.91f;
        this.lifetime = 42 + this.random.nextInt(28);
        this.quadSize = 1.8f + this.random.nextFloat() * 1.8f;
        this.rCol = 0.78f + this.random.nextFloat() * 0.12f;
        this.gCol = 0.66f + this.random.nextFloat() * 0.10f;
        this.bCol = 0.46f + this.random.nextFloat() * 0.08f;
        this.alpha = 0.82f;
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        float age = (float) this.age / (float) this.lifetime;
        this.alpha = Math.max(0.0f, 0.82f * (1.0f - age * age));
        this.quadSize *= 1.018f;
        this.setSpriteFromAge(this.sprites);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 240;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            return new SandImpactParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites);
        }
    }
}
