package net.jelly.sandworm_mod.brewing;

import net.jelly.sandworm_mod.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.CustomData;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WormToothBrewing implements IBrewingRecipe {
    @Override
    public boolean isInput(ItemStack input) {
        PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return false;
        boolean hasEffects = contents.potion().isPresent() || !contents.customEffects().isEmpty();
        CustomData customData = input.get(DataComponents.CUSTOM_DATA);
        boolean isDuneElixir = customData != null && customData.copyTag().getBoolean("duneElixir");
        return hasEffects || isDuneElixir;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.is(ModItems.WORM_TOOTH.get());
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        PotionContents contents = input.get(DataComponents.POTION_CONTENTS);
        if (contents == null) return input;

        List<MobEffectInstance> allEffects = new ArrayList<>();
        contents.getAllEffects().forEach(effect ->
                allEffects.add(new MobEffectInstance(
                        effect.getEffect(),
                        effect.getDuration(),
                        effect.getAmplifier() + 1,
                        effect.isAmbient(),
                        effect.isVisible(),
                        effect.showIcon()))
        );

        if (allEffects.isEmpty()) return input;

        ItemStack result = input.copy();
        // Replace potion contents with amplified custom effects, clearing base potion
        result.set(DataComponents.POTION_CONTENTS,
                new PotionContents(Optional.empty(), Optional.empty(), allEffects));

        // Mark as duneElixir via custom data
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("duneElixir", true);
        result.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));

        // Set custom display name
        result.set(DataComponents.CUSTOM_NAME,
                Component.literal("Dune Elixir").withStyle(style -> style.withColor(0xFFAA00).withItalic(false)));

        return result;
    }
}
