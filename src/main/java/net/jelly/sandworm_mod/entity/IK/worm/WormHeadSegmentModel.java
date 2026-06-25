package net.jelly.sandworm_mod.entity.IK.worm;

import net.jelly.sandworm_mod.SandwormMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WormHeadSegmentModel extends GeoModel<WormHeadSegment> {
    @Override
    public ResourceLocation getModelResource(WormHeadSegment wormHeadSegment) {
        return ResourceLocation.fromNamespaceAndPath(SandwormMod.MODID, "geo/worm_segment_head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WormHeadSegment wormHeadSegment) {
        return ResourceLocation.fromNamespaceAndPath(SandwormMod.MODID, "textures/entity/worm_head_segment_texture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WormHeadSegment wormHeadSegment) {
        return ResourceLocation.fromNamespaceAndPath(SandwormMod.MODID, "animations/no_animation.json");
    }
}
