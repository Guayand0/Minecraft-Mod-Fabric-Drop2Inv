package com.guayand0;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.tags.BlockTags;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class LeafBreakUtils {

    public static void breakNearbyLeaves(ServerLevel world, Player player, BlockPos start) {
        for (int i = 0; i < 3; i++) {
            breakLeavesPass(world, player, start);
        }
    }

    private static void breakLeavesPass(ServerLevel world, Player player, BlockPos start) {

        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        int radius = 6;

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;
            if (pos.distManhattan(start) > radius) continue;

            BlockState state = world.getBlockState(pos);

            if (state.is(BlockTags.LEAVES)) {
                DropUtils.giveDrops(world, player, pos, state, null);
                DropTracker.mark(pos);
                world.destroyBlock(pos, false);
            }

            for (int x = -1; x <= 1; x++)
                for (int y = -1; y <= 1; y++)
                    for (int z = -1; z <= 1; z++)
                        queue.add(pos.offset(x, y, z));
        }
    }
}
