package net.jelly.sandworm_mod.advancements;

import net.jelly.sandworm_mod.SandwormMod;
import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementTriggerRegistry {
    public static final AdvancementTrigger THUMPER = new AdvancementTrigger();
    public static final AdvancementTrigger SHAI_HULUD = new AdvancementTrigger();
    public static final AdvancementTrigger FIRST_BLAST = new AdvancementTrigger();
    public static final AdvancementTrigger SANDWORM_FLEE = new AdvancementTrigger();
    public static final AdvancementTrigger DUNE_ELIXIR = new AdvancementTrigger();

    public static void init() {
        CriteriaTriggers.register(SandwormMod.MODID + ":thumper", THUMPER);
        CriteriaTriggers.register(SandwormMod.MODID + ":shai_hulud", SHAI_HULUD);
        CriteriaTriggers.register(SandwormMod.MODID + ":first_blast", FIRST_BLAST);
        CriteriaTriggers.register(SandwormMod.MODID + ":sandworm_flee", SANDWORM_FLEE);
        CriteriaTriggers.register(SandwormMod.MODID + ":dune_elixir", DUNE_ELIXIR);
    }
}
