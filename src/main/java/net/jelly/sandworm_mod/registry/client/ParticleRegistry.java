package net.jelly.sandworm_mod.registry.client;

import net.jelly.sandworm_mod.SandwormMod;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ParticleRegistry {
    public static DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SandwormMod.MODID);

    public static final RegistryObject<SimpleParticleType> SAND_IMPACT =
            PARTICLES.register("sand_impact", () -> new SimpleParticleType(false));

    public static void register(IEventBus eventBus) {
        PARTICLES.register(eventBus);
    }
}
