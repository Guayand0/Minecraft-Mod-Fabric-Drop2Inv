package com.guayand0.drop;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;

import java.util.*;

public class LeafBreakUtils {

    public static void breakNearbyLeaves(ServerWorld world, PlayerEntity player, BlockPos start) {
        for (int i = 0; i < 3; i++) {
            breakLeavesPass(world, player, start);
        }
    }

    private static void breakLeavesPass(ServerWorld world, PlayerEntity player, BlockPos start) {

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        int radius = 6;

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;
            if (pos.getManhattanDistance(start) > radius) continue;

            BlockState state = world.getBlockState(pos);

            if (state.isIn(BlockTags.LEAVES)) {
                DropUtils.giveDrops(world, player, pos, state, null);
                DropTracker.mark(pos);
                world.breakBlock(pos, false);
            }

            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    for (int z = -1; z <= 1; z++)
                        queue.add(pos.add(x, y, z));
        }
    }
}
