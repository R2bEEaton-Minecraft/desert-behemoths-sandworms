package net.jelly.sandworm_mod.worldevents;

import net.jelly.sandworm_mod.network.SandwormNetwork;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SonicBoomWorldEvent {
    public static void spawn(Level level, Entity followEntity) {
        if (!(level instanceof ServerLevel serverLevel) || followEntity == null) return;

        Vec3 pos = followEntity.position();
        SandwormNetwork.SonicBoomPacket packet = SandwormNetwork.SonicBoomPacket.at(followEntity.getId(), pos);
        for (ServerPlayer player : serverLevel.players()) {
            if (player.distanceToSqr(pos) <= 120.0 * 120.0) {
                SandwormNetwork.sendToPlayer(player, packet);
            }
        }
    }
}
