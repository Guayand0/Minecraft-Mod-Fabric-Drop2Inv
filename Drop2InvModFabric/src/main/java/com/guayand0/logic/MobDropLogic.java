package com.guayand0.logic;

import net.minecraft.entity.ItemEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class MobDropLogic {

    public static void give(ServerPlayerEntity player, ItemEntity item) {
        if (player.getInventory().insertStack(item.getStack())) {
            item.discard();
        }
    }
}
