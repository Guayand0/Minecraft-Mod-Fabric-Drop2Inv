package com.guayand0.blocks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.ItemEntity;

public class DropCancelHandler {

    public static void register() {
        ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
            if (!(entity instanceof ItemEntity item)) return;
            if (!(world instanceof ServerWorld)) return;

            if (DropTracker.consume(item.getBlockPos())) {
                item.discard();
            }
        });
    }
}
