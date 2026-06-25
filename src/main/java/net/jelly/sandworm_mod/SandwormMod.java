package net.jelly.sandworm_mod;

import com.mojang.logging.LogUtils;
import net.jelly.sandworm_mod.advancements.AdvancementTriggerRegistry;
import net.jelly.sandworm_mod.block.ModBlockEntities;
import net.jelly.sandworm_mod.block.ModBlocks;
import net.jelly.sandworm_mod.block.thumper.ThumperRenderer;
import net.jelly.sandworm_mod.config.CommonConfigs;
import net.jelly.sandworm_mod.entity.IK.KinematicChainRenderer;
import net.jelly.sandworm_mod.entity.IK.worm.WormHeadSegmentRenderer;
import net.jelly.sandworm_mod.entity.IK.worm.WormSegmentRenderer;
import net.jelly.sandworm_mod.entity.ModEntities;
import net.jelly.sandworm_mod.item.ModItems;
import net.jelly.sandworm_mod.sound.ModSounds;
import net.jelly.sandworm_mod.registry.client.ParticleRegistry;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SandwormMod.MODID)
public class SandwormMod
{
    public static final String MODID = "sandworm_mod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public SandwormMod(IEventBus modEventBus)
    {
        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        ModSounds.register(modEventBus);
        ParticleRegistry.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfigs.SPEC, "sandwormmod-common.toml");

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        event.enqueueWork(() -> {
            AdvancementTriggerRegistry.init();
        });
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.WORM_SEGMENT.get(), WormSegmentRenderer::new);
            EntityRenderers.register(ModEntities.WORM_HEAD_SEGMENT.get(), WormHeadSegmentRenderer::new);
            EntityRenderers.register(ModEntities.WORM_CHAIN.get(), KinematicChainRenderer::new);
            BlockEntityRenderers.register(ModBlockEntities.THUMPER_ENTITY.get(), ThumperRenderer::new);
        }
    }
}
