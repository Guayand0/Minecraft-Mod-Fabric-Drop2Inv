package com.guayand0.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Drop2InvConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("drop2inv.json");

    private static Drop2InvConfig config;

    public static void load() {
        try {
            if (Files.notExists(CONFIG_PATH)) {
                config = Drop2InvConfig.DEFAULTS;
                save();
            } else {
                config = GSON.fromJson(Files.readString(CONFIG_PATH), Drop2InvConfig.class);
            }
        } catch (Exception e) {
            config = Drop2InvConfig.DEFAULTS;
            e.printStackTrace();
        }
    }

    public static void save() throws IOException {
        Files.writeString(CONFIG_PATH, GSON.toJson(config));
    }

    public static Drop2InvConfig get() {
        return config;
    }
}
