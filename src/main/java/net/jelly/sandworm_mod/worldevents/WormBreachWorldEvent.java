package net.jelly.sandworm_mod.worldevents;

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
        // Spawn a ring of sand particles around the breach point
        for (int i = 0; i < 36; i++) {
            double angle = i * (Math.PI * 2 / 36);
            double offsetX = Math.cos(angle) * 3.0;
            double offsetZ = Math.sin(angle) * 3.0;
            level.sendParticles(sandParticle,
                    position.x + offsetX, position.y + 0.5, position.z + offsetZ,
                    5, 0.5, 0.3, 0.5, 0.15);
        }
        // Extra burst at center
        level.sendParticles(sandParticle, position.x, position.y + 1, position.z, 30, 1.5, 0.5, 1.5, 0.2);
    }
}
