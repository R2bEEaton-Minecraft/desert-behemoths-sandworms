package net.jelly.sandworm_mod.registry.client;

import net.jelly.sandworm_mod.SandwormMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ParticleRegistry {
    public static DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SandwormMod.MODID);

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
