package net.jelly.sandworm_mod.vfx;

import net.jelly.sandworm_mod.network.SandwormNetwork;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClientVfxManager {
    private static final List<ShakeInstance> SHAKES = new ArrayList<>();
    private static final List<SonicBoomInstance> SONIC_BOOMS = new ArrayList<>();

    public static void addShake(SandwormNetwork.ScreenShakePacket packet) {
        SHAKES.add(new ShakeInstance(packet.duration(), new Vec3(packet.x(), packet.y(), packet.z()),
                packet.radius(), packet.intensity(), packet.endIntensity(), packet.positioned()));
    }

    public static void addSonicBoom(SandwormNetwork.SonicBoomPacket packet) {
        SONIC_BOOMS.add(new SonicBoomInstance(packet.entityId(), new Vec3(packet.x(), packet.y(), packet.z())));
    }

    public static void clientTick(TickEvent.ClientTickEvent.Post event) {
        SHAKES.removeIf(ShakeInstance::tick);

        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            SONIC_BOOMS.clear();
            return;
        }

        Iterator<SonicBoomInstance> iterator = SONIC_BOOMS.iterator();
        while (iterator.hasNext()) {
            SonicBoomInstance boom = iterator.next();
            if (boom.tick(level)) {
                iterator.remove();
            }
        }
    }

    public static void cameraAngles(ViewportEvent.ComputeCameraAngles event) {
        if (SHAKES.isEmpty()) return;

        Camera camera = event.getCamera();
        Vec3 cameraPos = camera.getPosition();
        double ticks = Minecraft.getInstance().level == null ? 0 : Minecraft.getInstance().level.getGameTime();
        double time = ticks + event.getPartialTick();
        float amount = 0.0f;

        for (ShakeInstance shake : SHAKES) {
            amount += shake.intensity(cameraPos);
        }

        amount = Math.min(amount, 1.25f);
        if (amount <= 0) return;
        float yaw = (float) (Math.sin(time * 3.91) * amount * 1.8f);
        float pitch = (float) (Math.sin(time * 4.73 + 1.7) * amount * 1.35f);
        float roll = (float) (Math.sin(time * 5.31 + 0.4) * amount * 1.15f);
        event.setYaw(event.getYaw() + yaw);
        event.setPitch(event.getPitch() + pitch);
        event.setRoll(event.getRoll() + roll);
    }

    private static class ShakeInstance {
        private final int duration;
        private final Vec3 position;
        private final float radius;
        private final float intensity;
        private final float endIntensity;
        private final boolean positioned;
        private int age;

        private ShakeInstance(int duration, Vec3 position, float radius, float intensity, float endIntensity, boolean positioned) {
            this.duration = Math.max(1, duration);
            this.position = position;
            this.radius = Math.max(1.0f, radius);
            this.intensity = intensity;
            this.endIntensity = endIntensity;
            this.positioned = positioned;
        }

        private boolean tick() {
            age++;
            return age >= duration;
        }

        private float intensity(Vec3 cameraPos) {
            float progress = Math.min(1.0f, (float) age / (float) duration);
            float eased = 1.0f - (1.0f - progress) * (1.0f - progress) * (1.0f - progress);
            float value = lerp(intensity, endIntensity, eased);
            if (!positioned) return value;

            double distance = cameraPos.distanceTo(position);
            if (distance >= radius) return 0.0f;
            float distanceFalloff = 1.0f - (float) (distance / radius);
            return value * distanceFalloff * distanceFalloff;
        }
    }

    private static class SonicBoomInstance {
        private final int entityId;
        private Vec3 position;
        private int age;

        private SonicBoomInstance(int entityId, Vec3 position) {
            this.entityId = entityId;
            this.position = position;
        }

        private boolean tick(ClientLevel level) {
            Entity entity = level.getEntity(entityId);
            if (entity != null) {
                position = entity.position();
            }

            if (age == 0) {
                level.addParticle(ParticleTypes.SONIC_BOOM, position.x, position.y, position.z, 0, 0, 0);
            }

            double radius = 1.25 + age * 0.65;
            int points = 18 + age / 2;
            for (int i = 0; i < points; i++) {
                double angle = (Math.PI * 2.0 * i / points) + age * 0.08;
                double x = position.x + Math.cos(angle) * radius;
                double z = position.z + Math.sin(angle) * radius;
                double y = position.y + Math.sin(age * 0.25 + i) * 0.15;
                level.addParticle(ParticleTypes.CLOUD, x, y, z,
                        Math.cos(angle) * 0.035, 0.002, Math.sin(angle) * 0.035);
            }

            age++;
            return age > 34;
        }
    }

    private static float lerp(float a, float b, float f) {
        return a * (1.0f - f) + b * f;
    }
}
