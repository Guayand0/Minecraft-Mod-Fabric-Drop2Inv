package com.guayand0;

import com.guayand0.config.compat.ClothConfigCompat;
import com.guayand0.config.Drop2InvConfig;
import com.guayand0.mobs.config.MobConfigLoader;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;

import java.nio.file.Path;

import static com.guayand0.Drop2Inv.MOD_ID;

public class Drop2InvClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(Drop2InvConfig.class, GsonConfigSerializer::new);

        Path configPath = FabricLoader.getInstance().getConfigDir().resolve(MOD_ID).resolve("mobs.json");
        MobConfigLoader.load(configPath);
    }

    public static Screen getConfigScreen(Screen parent) {
        return ClothConfigCompat.create(parent);
        /*if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ClothConfigCompat.create(parent);
        } else {
            return screen -> null;
        }*/
    }
}
