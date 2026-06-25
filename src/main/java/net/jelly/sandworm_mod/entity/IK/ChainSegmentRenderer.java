package net.jelly.sandworm_mod.entity.IK;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

// ChainSegment entities are invisible; WormSegment/WormHeadSegment have their own renderers.
public class ChainSegmentRenderer extends EntityRenderer<ChainSegment> {
    public ChainSegmentRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(ChainSegment pEntity) {
        return null;
    }
}
