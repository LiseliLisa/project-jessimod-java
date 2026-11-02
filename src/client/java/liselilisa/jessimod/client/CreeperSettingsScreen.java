
package liselilisa.jessimod.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class CreeperSettingsScreen extends Screen {

    private final Screen parent;

    public CreeperSettingsScreen(Screen parent) {
        super(Component.literal("Creeper Warning Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Enable / disable Creeper Warning
        this.addRenderableWidget(
                Button.builder(
                                Component.literal(getToggleText()),
                                button -> {
                                    ModFeatures.creeperWarningEnabled =
                                            !ModFeatures.creeperWarningEnabled;

                                    button.setMessage(
                                            Component.literal(getToggleText())
                                    );
                                }
                        )
                        .bounds(centerX - 100, centerY - 45, 200, 20)
                        .build()
        );

        // Sound volume slider
        this.addRenderableWidget(
                new VolumeSlider(
                        centerX - 100,
                        centerY - 10,
                        200
                )
        );

        // Test custom warning sound
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Test Sound"),
                                button -> CreeperWarningFeature.playWarning(
                                        this.minecraft
                                )
                        )
                        .bounds(centerX - 100, centerY + 25, 200, 20)
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

    private String getToggleText() {
        return "Creeper Warning: " +
                (ModFeatures.creeperWarningEnabled ? "ON" : "OFF");
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

        String heading = "Creeper Warning Settings";

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
        ModFeatures.save();

        // Return to the JessiMod main menu.
        this.minecraft.gui.setScreen(this.parent);
    }

    // Volume slider
    private static class VolumeSlider extends AbstractSliderButton {

        public VolumeSlider(int x, int y, int width) {
            super(
                    x,
                    y,
                    width,
                    20,
                    Component.empty(),
                    ModFeatures.creeperWarningVolume
            );

            updateMessage();
        }

        @Override
        protected void updateMessage() {

            int percentage = (int) Math.round(this.value * 100);

            this.setMessage(
                    Component.literal(
                            "Warning Volume: " + percentage + "%"
                    )
            );
        }

        @Override
        protected void applyValue() {
            ModFeatures.creeperWarningVolume =
                    (float) this.value;
        }
    }
}
