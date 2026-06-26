package net.jelly.sandworm_mod.worldevents;

import net.jelly.sandworm_mod.registry.client.ParticleRegistry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class WormBreachWorldEvent {
    private Vec3 position;

    public WormBreachWorldEvent setPosition(Vec3 pos) {
        this.position = pos;
        return this;
    }

    public void spawnParticles(ServerLevel level) {
        if (position == null) return;
        BlockParticleOption sandParticle = new BlockParticleOption(ParticleTypes.BLOCK, Blocks.SAND.defaultBlockState());

        for (int i = 0; i < 72; i++) {
            double angle = i * (Math.PI * 2 / 72);
            double offsetX = Math.cos(angle) * 3.5;
            double offsetZ = Math.sin(angle) * 3.5;
            level.sendParticles(sandParticle,
                    position.x + offsetX, position.y + 0.5, position.z + offsetZ,
                    7, 0.65, 0.5, 0.65, 0.22);
            level.sendParticles(ParticleRegistry.SAND_IMPACT.get(),
                    position.x + offsetX * 0.55, position.y + 0.8, position.z + offsetZ * 0.55,
                    1, 0.18, 0.12, 0.18, 0.035);
        }

        level.sendParticles(sandParticle, position.x, position.y + 1, position.z, 80, 2.0, 0.8, 2.0, 0.28);
        level.sendParticles(ParticleRegistry.SAND_IMPACT.get(), position.x, position.y + 1.2, position.z,
                36, 1.8, 0.5, 1.8, 0.06);
        level.sendParticles(ParticleTypes.POOF, position.x, position.y + 1.0, position.z,
                18, 1.5, 0.35, 1.5, 0.08);
    }
}
