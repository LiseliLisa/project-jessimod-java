
package liselilisa.jessimod.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModFeatures {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("jessimod.json");

    // Default values
    public static boolean creeperWarningEnabled = true;
    public static float creeperWarningVolume = 0.65F;

    private ModFeatures() {}

    public static void load() {

        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(
                CONFIG_FILE, StandardCharsets.UTF_8)) {

            JsonObject json = JsonParser.parseReader(reader)
                    .getAsJsonObject();

            if (json.has("creeperWarningEnabled")) {
                creeperWarningEnabled =
                        json.get("creeperWarningEnabled")
                                .getAsBoolean();
            }

            if (json.has("creeperWarningVolume")) {
                float loadedVolume =
                        json.get("creeperWarningVolume")
                                .getAsFloat();

                if (Float.isFinite(loadedVolume)) {
                    creeperWarningVolume = Math.max(
                            0.0F,
                            Math.min(1.0F, loadedVolume)
                    );
                }
            }

        } catch (IOException | RuntimeException e) {
            System.err.println(
                    "[JessiMod] Failed to load config: " + e
            );
        }
    }

    public static void save() {

        JsonObject json = new JsonObject();

        json.addProperty(
                "creeperWarningEnabled",
                creeperWarningEnabled
        );

        json.addProperty(
                "creeperWarningVolume",
                creeperWarningVolume
        );

        try {
            Files.createDirectories(
                    CONFIG_FILE.getParent()
            );

            try (Writer writer = Files.newBufferedWriter(
                    CONFIG_FILE, StandardCharsets.UTF_8)) {

                GSON.toJson(json, writer);
            }

        } catch (IOException e) {
            System.err.println(
                    "[JessiMod] Failed to save config: " + e
            );
        }
    }
}
