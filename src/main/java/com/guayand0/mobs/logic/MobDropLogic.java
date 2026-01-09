package com.guayand0.mobs.logic;

import com.guayand0.config.Drop2InvConfig;
import com.guayand0.config.Drop2InvConfigManager;
import com.guayand0.mobs.MobCategory;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MobDropLogic {

    /**
     * Da los ítems al jugador directamente según la categoría del mob
     */
    public static void give(ServerPlayerEntity player, ItemEntity item, MobCategory category) {

        Drop2InvConfig config = Drop2InvConfigManager.get();

        boolean allow;

        switch (category) {
            case HOSTILE -> allow = config.mobs.hostile;
            case NEUTRAL -> allow = config.mobs.neutral;
            case PASSIVE -> allow = config.mobs.passive;
            default -> {
                return;
            }
        }

        if (!allow) return; // deja caer vanilla

        if (player.getInventory().insertStack(item.getStack())) {
            item.discard();
        }
    }

}
