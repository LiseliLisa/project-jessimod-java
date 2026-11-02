
package liselilisa.jessimod.client;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class ModMenuScreen extends Screen {

    private final Screen parent;

    public ModMenuScreen(Screen parent) {
        super(Component.literal("JessiMod Settings"));
        this.parent = parent;
    }

    @Override
    protected void init() {

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        // Open Creeper Warning submenu
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Creeper Warning..."),
                                button -> this.minecraft.gui.setScreen(
                                        new CreeperSettingsScreen(this)
                                )
                        )
                        .bounds(centerX - 100, centerY - 20, 200, 20)
                        .build()
        );

        // Return to Minecraft pause menu
        this.addRenderableWidget(
                Button.builder(
                                Component.literal("Done"),
                                button -> this.onClose()
                        )
                        .bounds(centerX - 100, centerY + 20, 200, 20)
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

        String heading = "JessiMod Settings";

        graphics.text(
                this.font,
                heading,
                (this.width - this.font.width(heading)) / 2,
                this.height / 2 - 65,
                0xFFFFFFFF,
                true
        );
    }

    @Override
    public void onClose() {
        ModFeatures.save();
        this.minecraft.gui.setScreen(this.parent);
    }
}
