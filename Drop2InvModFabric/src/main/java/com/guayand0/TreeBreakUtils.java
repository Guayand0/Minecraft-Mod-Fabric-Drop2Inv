package com.guayand0;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TreeBreakUtils {

    public static boolean isLog(BlockState state) {
        return state.is(BlockTags.LOGS);
    }

    public static void breakTree(ServerLevel world, Player player, BlockPos start) {

        if (!(player instanceof ServerPlayer)) return;

        ItemStack axe = player.getMainHandItem();

        Set<BlockPos> visited = new HashSet<>();
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        // 1️⃣ Buscar todos los troncos
        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (!state.is(BlockTags.LOGS)) continue;

            logs.add(pos);

            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    for (int z = -1; z <= 1; z++)
                        queue.add(pos.offset(x, y, z));
        }

        // 2️⃣ Ordenar de arriba a abajo
        logs.stream()
                .sorted((a, b) -> Integer.compare(b.getY(), a.getY()))
                .forEach(pos -> {
                    if (axe.isEmpty()) return;

                    BlockState state = world.getBlockState(pos);

                    DropUtils.giveDrops(world, player, pos, state, null);
                    DropTracker.mark(pos);
                    world.destroyBlock(pos, false);

                    axe.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                });

        // 3️⃣ Leaf decay rápido
        LeafBreakUtils.breakNearbyLeaves(world, player, start);
    }

}
