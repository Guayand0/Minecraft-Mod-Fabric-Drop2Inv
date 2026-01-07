package com.guayand0.mobs.utils;

import com.guayand0.config.Drop2InvConfig;
import com.guayand0.mobs.MobCategory;
import com.guayand0.mobs.config.MobConfig;
import com.guayand0.mobs.config.MobConfigLoader;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class MobUtils {

    public static MobCategory getCategory(EntityType<?> type) {

        Identifier id = Registries.ENTITY_TYPE.getId(type);
        String mobId = id.toString();

        Drop2InvConfig config = AutoConfig.getConfigHolder(Drop2InvConfig.class).getConfig();
        MobConfig json = MobConfigLoader.get();

        // 1️⃣ override por mob (usuario)
        if (config.mobs.individual_category.containsKey(mobId)) {
            return config.mobs.individual_category.get(mobId);
        }

        // 2️⃣ default desde mobs.json
        return getDefaultCategory(mobId, json);
    }

    public static MobCategory getDefaultCategory(String mobId, MobConfig json) {
        if (json.passive.contains(mobId)) return MobCategory.PASSIVE;
        if (json.neutral.contains(mobId)) return MobCategory.NEUTRAL;
        if (json.hostile.contains(mobId)) return MobCategory.HOSTILE;
        return MobCategory.NEUTRAL;
    }
}
