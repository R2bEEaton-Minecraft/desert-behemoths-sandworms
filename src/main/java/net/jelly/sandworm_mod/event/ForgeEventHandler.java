package net.jelly.sandworm_mod.event;

import net.jelly.sandworm_mod.SandwormMod;
import net.jelly.sandworm_mod.advancements.AdvancementTriggerRegistry;
import net.jelly.sandworm_mod.capabilities.wormsign.WormSign;
import net.jelly.sandworm_mod.capabilities.wormsign.WormSignProvider;
import net.jelly.sandworm_mod.config.CommonConfigs;
import net.jelly.sandworm_mod.entity.IK.worm.WormChainEntity;
import net.jelly.sandworm_mod.entity.IK.worm.WormHeadSegment;
import net.jelly.sandworm_mod.entity.ModEntities;
import net.jelly.sandworm_mod.sound.ModSounds;
import net.jelly.sandworm_mod.brewing.WormToothBrewing;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraftforge.event.brewing.BrewingRecipeRegisterEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.brewing.PlayerBrewedPotionEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

import static net.jelly.sandworm_mod.helper.BiomeHelper.isDesertBiome;

@Mod.EventBusSubscriber(modid = SandwormMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEventHandler {

    @SubscribeEvent
    public static void RegisterModCommands(RegisterCommandsEvent event) {
        com.mojang.brigadier.CommandDispatcher<net.minecraft.commands.CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(
            net.minecraft.commands.Commands.literal("spawnworm")
                .requires(src -> src.hasPermission(2))
                .executes(ctx -> {
                    net.minecraft.commands.CommandSourceStack src = ctx.getSource();
                    net.minecraft.world.phys.Vec3 pos = src.getPosition();
                    WormChainEntity worm = new WormChainEntity(ModEntities.WORM_CHAIN.get(), src.getLevel());
                    worm.moveTo(pos.x, pos.y, pos.z);
                    src.getLevel().addFreshEntity(worm);
                    src.sendSuccess(() -> net.minecraft.network.chat.Component.literal("Spawned sandworm at " + (int)pos.x + " " + (int)pos.y + " " + (int)pos.z), true);
                    return 1;
                })
        );
    }

    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            if (!event.getObject().getCapability(WormSignProvider.WS).isPresent()) {
                event.addCapability(ResourceLocation.fromNamespaceAndPath(SandwormMod.MODID, "properties"), new WormSignProvider());
            }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide()) return;
        Vec3 pos = event.getExplosion().center();
        List<WormHeadSegment> hitHeads = event.getLevel().getEntitiesOfClass(WormHeadSegment.class,
                new AABB(pos.x + 5, pos.y + 5, pos.z + 5, pos.x - 5, pos.y - 5, pos.z - 5));
        if (!hitHeads.isEmpty()) hitHeads.forEach(head -> {
            head.playSound(ModSounds.WORM_ROAR.get(), 10f, 1f);
            WormChainEntity wormChain = head.getOwner();
            if (wormChain != null) wormChain.blastHit();
            Entity sourcePlayer = event.getExplosion().getIndirectSourceEntity();
            if (sourcePlayer == null) {
                Vec3 explosionPos = event.getExplosion().center();
                sourcePlayer = event.getLevel().getNearestPlayer(explosionPos.x, explosionPos.y, explosionPos.z, 100.0, true);
            }
            if (sourcePlayer instanceof Player) {
                AdvancementTriggerRegistry.FIRST_BLAST.trigger((ServerPlayer) sourcePlayer);
                if (wormChain != null && wormChain.explodedTimes >= CommonConfigs.HEALTH.get())
                    AdvancementTriggerRegistry.SANDWORM_FLEE.trigger((ServerPlayer) sourcePlayer);
            }
        });
    }

    @SubscribeEvent
    public static void onRegisterBrewingRecipes(BrewingRecipeRegisterEvent event) {
        event.addRecipe(new WormToothBrewing());
    }

    @SubscribeEvent
    public static void brewPotion(PlayerBrewedPotionEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        CustomData customData = event.getStack().get(DataComponents.CUSTOM_DATA);
        if (customData != null && customData.copyTag().getBoolean("duneElixir")) {
            AdvancementTriggerRegistry.DUNE_ELIXIR.trigger((ServerPlayer) event.getEntity());
        }
    }
}
