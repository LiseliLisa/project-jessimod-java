package liselilisa.jessimod.client.mixin;

import liselilisa.jessimod.client.SoundSettings.SoundFileHandler;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.client.resources.sounds.SoundInstance;

import net.minecraft.sounds.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundManager.class)
public class SoundBlockerMixin {
    String villager_ambient = SoundEvents.VILLAGER_AMBIENT.location().toShortString();
    String portal_ambient = SoundEvents.PORTAL_AMBIENT.location().toShortString();
    String beacon_ambient = SoundEvents.BEACON_AMBIENT.location().toShortString();

    @Inject(method = "play", at = @At("HEAD"), cancellable = true)
    private void onPlaySound(SoundInstance instance, CallbackInfoReturnable<SoundEngine.PlayResult> info) {
        if (instance != null) {
            String currentSoundId = instance.getIdentifier().toShortString();

            if (currentSoundId.equals(villager_ambient) && !SoundFileHandler.villagerSoundEnabled)  info.cancel();
            if (currentSoundId.equals(portal_ambient) && !SoundFileHandler.portalSoundEnabled)  info.cancel();
            if (currentSoundId.equals(beacon_ambient) && !SoundFileHandler.beaconSoundEnabled)  info.cancel();
        }
    }
}
