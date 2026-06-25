package net.jelly.sandworm_mod.worldevents;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class WormRippleWorldEvent {
    private Vec3 position;
    private int lifetime = 30;
    private final int scale = 2;

    public WormRippleWorldEvent spawnRipple(Vec3 pos) {
        this.position = pos;
        return this;
    }

    public boolean tick(ServerLevel level) {
        if (position == null || lifetime <= 0) return false;
        spawnSpiralParticles(level, position, lifetime);
        lifetime--;
        return true;
    }

    public boolean isDone() {
        return lifetime <= 0;
    }

    private void spawnSpiralParticles(ServerLevel level, Vec3 pos, int lifetime) {
        for (int t = 0; t < 360; t++) {
            if (t % 10 == 0) {
                int theta = t - 12 * lifetime;
                Vec3 particlePos = new Vec3(
                        pos.x + scale * (lifetime / 30.0d) * t / 20 * Math.cos(Math.toRadians(theta)),
                        pos.y,
                        pos.z + scale * (lifetime / 30.0d) * t / 20 * Math.sin(Math.toRadians(theta))
                );
                spawnParticle(level, particlePos);
            }
        }
    }

    private void spawnParticle(ServerLevel level, Vec3 pos) {
        int heightDecrement = 0;
        BlockPos blockPos = BlockPos.containing(pos.x, pos.y - 1, pos.z);
        BlockState blockState = level.getBlockState(blockPos);
        while (blockState.isAir()) {
            blockPos = BlockPos.containing(pos.x, pos.y - 1 - heightDecrement, pos.z);
            blockState = level.getBlockState(blockPos);
            heightDecrement++;
            if (heightDecrement >= 20) break;
        }

        BlockParticleOption blockParticle = new BlockParticleOption(ParticleTypes.BLOCK, blockState);
        level.sendParticles(blockParticle, pos.x, blockPos.getY() + 1, pos.z, 1, 0.2, 0.0, 0.2, 0.0);
    }
}
