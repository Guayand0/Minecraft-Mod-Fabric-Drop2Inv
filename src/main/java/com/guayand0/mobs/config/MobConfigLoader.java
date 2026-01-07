package com.guayand0.mobs.config;

import com.google.gson.Gson;
import com.guayand0.config.Drop2InvConfig;
import com.guayand0.mobs.MobCategory;
import me.shedaniel.autoconfig.AutoConfig;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class MobConfigLoader {

    private static MobConfig config;

    public static void load(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());

                try (InputStream in = MobConfigLoader.class.getClassLoader().getResourceAsStream("mobs.default.json")) {
                    Files.copy(in, path);
                }
            }
            config = new Gson().fromJson(Files.readString(path), MobConfig.class);

            Drop2InvConfig cfg = AutoConfig.getConfigHolder(Drop2InvConfig.class).getConfig();
            if (cfg.mobs.individual_category == null) {
                cfg.mobs.individual_category = new HashMap<>();
            }
            MobConfig json = MobConfigLoader.get();
            json.passive.forEach(id -> cfg.mobs.individual_category.putIfAbsent(id, MobCategory.PASSIVE));
            json.neutral.forEach(id -> cfg.mobs.individual_category.putIfAbsent(id, MobCategory.NEUTRAL));
            json.hostile.forEach(id -> cfg.mobs.individual_category.putIfAbsent(id, MobCategory.HOSTILE));

        } catch (Exception e) {
            throw new RuntimeException("Error cargando mobs.json", e);
        }
    }

    public static MobConfig get() {
        return config;
    }
}
