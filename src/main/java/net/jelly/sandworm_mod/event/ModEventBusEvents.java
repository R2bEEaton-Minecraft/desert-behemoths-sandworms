package net.jelly.sandworm_mod.event;

import net.jelly.sandworm_mod.SandwormMod;
import net.jelly.sandworm_mod.capabilities.wormsign.WormSign;
import net.jelly.sandworm_mod.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.Optional;

@Mod.EventBusSubscriber(modid = SandwormMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.register(WormSign.class);
    }

    // Explicitly register the mod's data directory as a SERVER_DATA pack.
    // In Forge 1.21.1 dev environments (directory-based mod files), mod data
    // is not automatically picked up by the server's pack repository.
    @SubscribeEvent
    public static void addDataPacks(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.SERVER_DATA) {
            var modFile = ModList.get().getModFileById(SandwormMod.MODID).getFile();
            event.addRepositorySource(consumer -> {
                var info = new PackLocationInfo(
                        "mod/" + SandwormMod.MODID,
                        Component.literal("Sandworm Mod Data"),
                        PackSource.DEFAULT,
                        Optional.empty()
                );
                Pack.ResourcesSupplier supplier = new Pack.ResourcesSupplier() {
                    @Override
                    public PackResources openPrimary(PackLocationInfo loc) {
                        return new PathPackResources(loc, modFile.findResource("."));
                    }
                    @Override
                    public PackResources openFull(PackLocationInfo loc, Pack.Metadata meta) {
                        return new PathPackResources(loc, modFile.findResource("."));
                    }
                };
                Pack pack = Pack.readMetaAndCreate(
                        info, supplier, PackType.SERVER_DATA,
                        new PackSelectionConfig(true, Pack.Position.BOTTOM, false)
                );
                if (pack != null) consumer.accept(pack);
            });
        }
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.WORM_TOOTH);
        } else if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS || event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.THUMPER_ITEM);
        }
    }
}
