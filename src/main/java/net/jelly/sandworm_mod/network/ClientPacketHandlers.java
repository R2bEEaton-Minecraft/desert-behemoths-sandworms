package net.jelly.sandworm_mod.network;

import net.jelly.sandworm_mod.vfx.ClientVfxManager;

public class ClientPacketHandlers {
    public static void handleScreenShake(SandwormNetwork.ScreenShakePacket packet) {
        ClientVfxManager.addShake(packet);
    }

    public static void handleSonicBoom(SandwormNetwork.SonicBoomPacket packet) {
        ClientVfxManager.addSonicBoom(packet);
    }
}
