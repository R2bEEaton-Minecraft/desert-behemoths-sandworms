package net.jelly.sandworm_mod.event;

import net.jelly.sandworm_mod.SandwormMod;
import net.jelly.sandworm_mod.registry.client.ParticleRegistry;
import net.jelly.sandworm_mod.vfx.ClientVfxManager;
import net.jelly.sandworm_mod.vfx.SandImpactParticle;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid = SandwormMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent.Post event) {
            ClientVfxManager.clientTick(event);
        }

        @SubscribeEvent
        public static void onCameraAngles(ViewportEvent.ComputeCameraAngles event) {
            ClientVfxManager.cameraAngles(event);
        }
    }

    @Mod.EventBusSubscriber(modid = SandwormMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModClientEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void registerParticles(RegisterParticleProvidersEvent event) {
            event.registerSpriteSet(ParticleRegistry.SAND_IMPACT.get(), SandImpactParticle.Provider::new);
        }
    }
}
