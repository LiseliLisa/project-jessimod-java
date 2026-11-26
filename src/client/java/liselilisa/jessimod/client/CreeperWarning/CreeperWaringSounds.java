
package liselilisa.jessimod.client.CreeperWarning;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public final class CreeperWaringSounds {

    public static final SoundEvent CREEPER_WARNING =
            register("creeper_warning");

    private CreeperWaringSounds() {}

    private static SoundEvent register(String name) {
        Identifier id = Identifier.fromNamespaceAndPath(
                "jessimod", name
        );

        return Registry.register(
                BuiltInRegistries.SOUND_EVENT,
                id,
                SoundEvent.createVariableRangeEvent(id)
        );
    }

    public static void initialize() {
        // Forces this class to initialize and register its sounds.
    }
}
