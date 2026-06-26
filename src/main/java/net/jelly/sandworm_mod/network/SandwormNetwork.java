package net.jelly.sandworm_mod.network;

import net.jelly.sandworm_mod.SandwormMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class SandwormNetwork {
    private static final int PROTOCOL_VERSION = 1;

    public static final SimpleChannel CHANNEL = ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(SandwormMod.MODID, "main"))
            .networkProtocolVersion(PROTOCOL_VERSION)
            .acceptedVersions(Channel.VersionTest.exact(PROTOCOL_VERSION))
            .simpleChannel();

    public static void register() {
        CHANNEL.messageBuilder(ScreenShakePacket.class, 0)
                .encoder(ScreenShakePacket::encode)
                .decoder(ScreenShakePacket::decode)
                .consumerMainThread((packet, context) ->
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                                ClientPacketHandlers.handleScreenShake(packet)))
                .add();

        CHANNEL.messageBuilder(SonicBoomPacket.class, 1)
                .encoder(SonicBoomPacket::encode)
                .decoder(SonicBoomPacket::decode)
                .consumerMainThread((packet, context) ->
                        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                                ClientPacketHandlers.handleSonicBoom(packet)))
                .add();

        CHANNEL.build();
    }

    public static void sendToPlayer(ServerPlayer player, Object packet) {
        CHANNEL.send(packet, PacketDistributor.PLAYER.with(player));
    }

    public record ScreenShakePacket(int duration, double x, double y, double z, float radius,
                                    float intensity, float endIntensity, boolean positioned) {
        public static ScreenShakePacket global(int duration, float intensity, float endIntensity) {
            return new ScreenShakePacket(duration, 0, 0, 0, 0, intensity, endIntensity, false);
        }

        public static ScreenShakePacket positioned(int duration, Vec3 pos, float radius, float intensity, float endIntensity) {
            return new ScreenShakePacket(duration, pos.x, pos.y, pos.z, radius, intensity, endIntensity, true);
        }

        private static void encode(ScreenShakePacket packet, FriendlyByteBuf buffer) {
            buffer.writeInt(packet.duration);
            buffer.writeDouble(packet.x);
            buffer.writeDouble(packet.y);
            buffer.writeDouble(packet.z);
            buffer.writeFloat(packet.radius);
            buffer.writeFloat(packet.intensity);
            buffer.writeFloat(packet.endIntensity);
            buffer.writeBoolean(packet.positioned);
        }

        private static ScreenShakePacket decode(FriendlyByteBuf buffer) {
            return new ScreenShakePacket(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(),
                    buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readBoolean());
        }
    }

    public record SonicBoomPacket(int entityId, double x, double y, double z) {
        public static SonicBoomPacket at(int entityId, Vec3 pos) {
            return new SonicBoomPacket(entityId, pos.x, pos.y, pos.z);
        }

        private static void encode(SonicBoomPacket packet, FriendlyByteBuf buffer) {
            buffer.writeInt(packet.entityId);
            buffer.writeDouble(packet.x);
            buffer.writeDouble(packet.y);
            buffer.writeDouble(packet.z);
        }

        private static SonicBoomPacket decode(FriendlyByteBuf buffer) {
            return new SonicBoomPacket(buffer.readInt(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
        }
    }
}
