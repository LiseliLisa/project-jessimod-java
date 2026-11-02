
package liselilisa.jessimod.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

public final class CreeperWarningFeature {

    private static final double DETECTION_RANGE = 16.0;
    private static final int CHECK_INTERVAL = 5;
    private static final int REQUIRED_CHECKS = 2;

    private static final Map<Integer, CreeperState> tracked =
            new HashMap<>();

    private static ClientLevel lastLevel;
    private static int ticks = 0;
    private static int soundCooldown = 0;

    private CreeperWarningFeature() {}

    public static void tick(Minecraft client) {

        // Reset tracking when changing worlds.
        if (client.level != lastLevel) {
            tracked.clear();
            lastLevel = client.level;
            ticks = 0;
            soundCooldown = 0;
        }

        if (client.player == null || client.level == null ||
                !ModFeatures.creeperWarningEnabled) {
            tracked.clear();
            return;
        }

        if (client.isPaused()) return;

        if (soundCooldown > 0) soundCooldown--;

        // Check every 5 ticks rather than every tick.
        if (++ticks % CHECK_INTERVAL != 0) return;

        Set<Integer> nearby = new HashSet<>();

        for (Creeper creeper : client.level.getEntitiesOfClass(
                Creeper.class,
                client.player.getBoundingBox().inflate(DETECTION_RANGE)
        )) {

            if (!creeper.isAlive()) continue;

            Vec3 position = creeper.position();
            Vec3 toPlayer = client.player.position()
                    .subtract(position);

            // Horizontal distance to the player.
            double distance = Math.hypot(
                    toPlayer.x, toPlayer.z
            );

            if (distance > DETECTION_RANGE ||
                    distance < 2.5 ||
                    Math.abs(toPlayer.y) > 4.0) {
                continue;
            }

            int id = creeper.getId();
            nearby.add(id);

            CreeperState state = tracked.computeIfAbsent(
                    id,
                    ignored -> new CreeperState(position)
            );

            // How much did the creeper actually move?
            Vec3 movement = position.subtract(state.lastPosition);
            state.lastPosition = position;

            double speed = Math.hypot(movement.x, movement.z);

            // Dot product: positive means movement toward us.
            double dot = movement.x * toPlayer.x
                    + movement.z * toPlayer.z;

            boolean approaching = speed > 0.12
                    && dot > 0.75 * speed * distance;

            if (approaching) {
                state.approachChecks++;
            } else {
                state.approachChecks = 0;
            }

            if (state.approachChecks >= REQUIRED_CHECKS
                    && !state.warned
                    && soundCooldown == 0) {

                playWarning(client);

                state.warned = true;
                soundCooldown = 40;
            }
        }

        // Forget creepers that have left the detection area.
        tracked.keySet().retainAll(nearby);
    }


    public static void playWarning(Minecraft client) {

        float volume = ModFeatures.creeperWarningVolume;

        if (volume <= 0.0F) {
            return;
        }

        client.getSoundManager().play(
                SimpleSoundInstance.forUI(
                        JessiModSounds.CREEPER_WARNING,
                        1.0F,   // Pitch
                        volume  // Volume (0.0 - 1.0)
                )
        );
    }


    private static final class CreeperState {

        private Vec3 lastPosition;
        private int approachChecks = 0;
        private boolean warned = false;

        private CreeperState(Vec3 position) {
            this.lastPosition = position;
        }
    }
}
