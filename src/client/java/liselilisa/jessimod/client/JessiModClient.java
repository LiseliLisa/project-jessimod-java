package liselilisa.jessimod.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;

import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class JessiModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ModFeatures.load();
		JessiModSounds.initialize();

		ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {

			if (screen instanceof PauseScreen) {

				Button modButton = Button.builder(
								Component.literal("Jessi Mod"),
								button -> client.gui.setScreen(
										new ModMenuScreen(screen)
								)
						)
						.bounds(width - 112, 10, 100, 20)
						.build();

				Screens.getWidgets(screen).add(modButton);
			}
		});

		ClientTickEvents.END_CLIENT_TICK.register(
				CreeperWarningFeature::tick
		);
	}
}
