package com.guayand0.drop;

import com.guayand0.utils.DropUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;

import java.util.*;

public class TreeBreakHandler {

    public static boolean isLog(BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }

    public static void breakTree(ServerWorld world, PlayerEntity player, BlockPos start) {

        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        ItemStack axe = player.getMainHandStack();

        Set<BlockPos> visited = new HashSet<>();
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (!state.isIn(BlockTags.LOGS)) continue;

            logs.add(pos);

            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    for (int z = -1; z <= 1; z++)
                        queue.add(pos.add(x, y, z));
        }

        logs.stream()
                .sorted((a, b) -> Integer.compare(b.getY(), a.getY()))
                .forEach(pos -> {
                    if (axe.isEmpty()) return;

                    BlockState state = world.getBlockState(pos);

                    DropUtils.giveDrops(world, player, pos, state, null);
                    DropTracker.mark(pos);
                    world.breakBlock(pos, false);

                    axe.damage(1, serverPlayer, EquipmentSlot.MAINHAND);
                });

        LeafBreakHandler.breakNearbyLeaves(world, player, start);
    }
}
