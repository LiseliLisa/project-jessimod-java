package liselilisa.jessimod.client.SoundSettings;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class SoundsSettingScreen extends Screen{
    private final Screen parent;

    public SoundsSettingScreen(Screen parent) {
        super(Component.literal("Sound Settings"));
        this.parent = parent;

    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Villager Sound: " +
                                        (SoundFileHandler.villagerSoundEnabled ? "ON" : "OFF")),
                                button -> {
                                    SoundFileHandler.villagerSoundEnabled =
                                            !SoundFileHandler.villagerSoundEnabled;
                                    
                                    button.setMessage(
                                            Component.literal("Villager Sound: " +
                                                    (SoundFileHandler.villagerSoundEnabled ? "ON" : "OFF"))
                                    );
                                }
                        )
                        .bounds(centerX - 100, centerY - 70, 200, 20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Portal Sound: " +
                                        (SoundFileHandler.portalSoundEnabled ? "ON" : "OFF")),
                                button -> {
                                    SoundFileHandler.portalSoundEnabled =
                                            !SoundFileHandler.portalSoundEnabled;

                                    button.setMessage(
                                            Component.literal("Portal Sound: " +
                                                    (SoundFileHandler.portalSoundEnabled ? "ON" : "OFF"))
                                    );
                                }
                        )
                        .bounds(centerX - 100, centerY - 45, 200, 20)
                        .build()
        );
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Beacon Sound: " +
                                        (SoundFileHandler.beaconSoundEnabled ? "ON" : "OFF")),
                                button -> {
                                    SoundFileHandler.beaconSoundEnabled =
                                            !SoundFileHandler.beaconSoundEnabled;

                                    button.setMessage(
                                            Component.literal("Beacon Sound: " +
                                                    (SoundFileHandler.beaconSoundEnabled ? "ON" : "OFF"))
                                    );
                                }
                        )
                        .bounds(centerX - 100, centerY - 20, 200, 20)
                        .build()
        );

        // Return to JessiMod main menu
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Back"),
                                button -> this.onClose()
                        )
                        .bounds(centerX - 100, centerY + 65, 200, 20)
                        .build()
        );
    }

    @Override
    public void extractRenderState(
            GuiGraphicsExtractor graphics,
            int mouseX,
            int mouseY,
            float delta
    ) {
        super.extractRenderState(
                graphics, mouseX, mouseY, delta
        );

        String heading = "Sound Settings";

        graphics.text(
                this.font,
                heading,
                (this.width - this.font.width(heading)) / 2,
                this.height / 2 - 90,
                0xFFFFFFFF,
                true
        );
    }

    @Override
    public void onClose() {

        // Save settings when leaving this submenu.
        SoundFileHandler.save();

        // Return to the JessiMod main menu.
        this.minecraft.gui.setScreen(this.parent);
    }
}
