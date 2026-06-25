package net.jelly.sandworm_mod.entity.IK.worm;

import net.jelly.sandworm_mod.config.CommonConfigs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;

public class WormHeadSegment extends WormSegment implements GeoEntity {
    public WormHeadSegment(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected double getDamage() { return super.getDamage() * CommonConfigs.HEAD_MULTIPLIER.get(); }

    @Override
    protected Vec3 getKB() { return new Vec3(5, 2, 5); }
}
