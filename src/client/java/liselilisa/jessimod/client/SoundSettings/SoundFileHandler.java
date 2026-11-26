package liselilisa.jessimod.client.SoundSettings;

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

public class SoundFileHandler {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private static final Path CONFIG_FILE =
            FabricLoader.getInstance()
                    .getConfigDir()
                    .resolve("JessiMod_SoundSettings.json");

    public static boolean villagerSoundEnabled = true;
    public static boolean portalSoundEnabled = true;
    public static boolean beaconSoundEnabled = true;

    public static void load() {

        if (!Files.exists(CONFIG_FILE)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(
                CONFIG_FILE, StandardCharsets.UTF_8)) {

            JsonObject json = JsonParser.parseReader(reader)
                    .getAsJsonObject();

            if (json.has("villagerSoundEnabled")) {
                villagerSoundEnabled =
                        json.get("villagerSoundEnabled")
                                .getAsBoolean();
            }
            if (json.has("villagerSoundEnabled")) {
                portalSoundEnabled =
                        json.get("portalSoundEnabled")
                                .getAsBoolean();
            }
            if (json.has("villagerSoundEnabled")) {
                beaconSoundEnabled =
                        json.get("beaconSoundEnabled")
                                .getAsBoolean();
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
                "villagerSoundEnabled",
                villagerSoundEnabled
        );
        json.addProperty(
                "portalSoundEnabled",
                portalSoundEnabled
        );
        json.addProperty(
                "beaconSoundEnabled",
                beaconSoundEnabled
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
