package net.jelly.sandworm_mod.datagen;

import net.jelly.sandworm_mod.SandwormMod;
import net.jelly.sandworm_mod.item.ModItems;
import net.jelly.sandworm_mod.registry.common.DamageTypesRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SandwormMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModDataGenHandler {
    private static final RegistrySetBuilder BUILDER =
            new RegistrySetBuilder().add(Registries.DAMAGE_TYPE, DamageTypesRegistry::bootstrap);

    @SubscribeEvent
    public static void gatherDataEvent(GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        PackOutput packOutput = dataGenerator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        dataGenerator.addProvider(event.includeServer(), new DatapackBuiltinEntriesProvider(
                packOutput, lookupProvider, BUILDER, Set.of(SandwormMod.MODID)));

        dataGenerator.addProvider(event.includeServer(), new SandwormRecipeProvider(packOutput, lookupProvider));
    }

    static class SandwormRecipeProvider extends RecipeProvider {
        SandwormRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected void buildRecipes(RecipeOutput output) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.THUMPER_ITEM.get())
                    .pattern("P")
                    .pattern("R")
                    .define('P', Items.PISTON)
                    .define('R', Items.REPEATER)
                    .unlockedBy("has_piston", has(Items.PISTON))
                    .save(output);
        }
    }
}
