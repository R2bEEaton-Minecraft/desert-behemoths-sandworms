# EFFECTS.md — Temporarily Disabled Visual Effects

This document records every visual-effect call that was commented out, where to find it, and what restoring it requires.

---

## 1. Screen-shake warning cue

**File:** `src/main/java/net/jelly/sandworm_mod/event/WormSignHandler.java`

| Line | Original call |
|------|--------------|
| 117 | `warningScreenshake(player, 0.5, ModSounds.WORM_WARNING_1.get(), ws.getStage(), ws.getWS());` |
| 122 | `warningScreenshake(player, 0.6, ModSounds.WORM_WARNING_2.get(), ws.getStage(), ws.getWS());` |

**What it does:** Fires a screen-shake and warning sound at worm-sign thresholds 50 % and 80 % of the spawn value. The shake portion was already stripped in the 1.21.1 port (Lodestone has no Forge 1.21.1 release); what remains in `warningScreenshake()` is just the `playSeededSound` call. Commenting these lines therefore also silences the warning audio cues.

**Implementation:** `WarningSpawnHelper.warningScreenshake()` at
`src/main/java/net/jelly/sandworm_mod/helper/WarningSpawnHelper.java:20`

**To restore:** Un-comment lines 117 and 122 in `WormSignHandler.java`. If the Lodestone screen-shake API becomes available for Forge 1.21.1 it can be re-added inside `warningScreenshake()`.

---

## 2. Sonic boom post-processing shader

**Status: already removed prior to this change.**

The Lodestone-based sonic boom ripple shader has no Forge 1.21.1 port.
The relevant stub classes are kept for reference only:

- `src/main/java/net/jelly/sandworm_mod/vfx/SonicBoomFx.java` — empty stub
- `src/main/java/net/jelly/sandworm_mod/vfx/SonicBoomPostProcessor.java` — empty stub
- `src/main/java/net/jelly/sandworm_mod/worldevents/SonicBoomWorldEvent.java` — empty stub
- Shader assets still present at `src/main/resources/assets/sandworm_mod/shaders/post/sonic_boom_post.json`
  and `src/main/resources/assets/sandworm_mod/shaders/program/sonic_boom.json`

The call site in `WormChainEntity.blastHit()` (line 374) already has a comment noting the removal.

---

## 3. Breach / land smoke particles

**File:** `src/main/java/net/jelly/sandworm_mod/entity/IK/worm/WormChainEntity.java`

| Line | Original call | Trigger |
|------|--------------|---------|
| 164 | `smokeParticles(particlePos.add(0, -9, 0));` | Worm surfaces (breach) |
| 169 | `smokeParticles(particlePos.add(0, -9, 0));` | Worm re-submerges (land) |

**What it does:** Spawns a server-side `WormBreachWorldEvent` that calls `ServerLevel.sendParticles()` with `BLOCK` particles sampled from the block directly below the breach position, producing a sand-splash effect on surfacing and re-entry.

**Implementation:** `WormChainEntity.smokeParticles()` at line 396; dispatches to
`src/main/java/net/jelly/sandworm_mod/worldevents/WormBreachWorldEvent.java`

**To restore:** Un-comment lines 164 and 169 in `WormChainEntity.java`.

---

## 4. Sand ripple world event (sinkHole)

**File:** `src/main/java/net/jelly/sandworm_mod/entity/IK/worm/WormChainEntity.java`

| Line | Original call |
|------|--------------|
| 198 | `else if (stage == 0) sinkHole(goal);` |
| 174–176 | `if (this.level() instanceof ServerLevel serverLevel) { activeRipples.removeIf(ripple -> !ripple.tick(serverLevel)); }` |

**What it does:** When the worm is circling its target in stage 0 (`sinkHole`), a `WormRippleWorldEvent` is added to `activeRipples`. Each server tick that event emits a decaying spiral of `BLOCK` particles around the target position for 30 ticks, creating a sand-ripple visual. The tick block (lines 174–176) drives those events; both must be re-enabled together.

**Implementation:** `WormChainEntity.sinkHole()` at line 403; event logic at
`src/main/java/net/jelly/sandworm_mod/worldevents/WormRippleWorldEvent.java`

**To restore:** Un-comment the `sinkHole(goal)` call at line 198 **and** the `activeRipples.removeIf(...)` block at lines 174–176 in `WormChainEntity.java`.
