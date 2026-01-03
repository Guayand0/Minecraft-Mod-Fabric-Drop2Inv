package com.guayand0.drop;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class VerticalBreakUtils {

    public static boolean isVerticalBlock(Block block) {
        return block == Blocks.BAMBOO ||
                block == Blocks.SUGAR_CANE ||
                block == Blocks.CACTUS ||
                block == Blocks.KELP_PLANT;
    }

    public static void breakVertical(ServerWorld world, PlayerEntity player, BlockPos pos, Block type) {
        BlockPos current = pos;

        while (true) {
            BlockState state = world.getBlockState(current);
            Block block = state.getBlock();

            if (type == Blocks.KELP_PLANT) {
                if (block != Blocks.KELP && block != Blocks.KELP_PLANT) break;
            } else {
                if (block != type) break;
            }

            DropUtils.giveDrops(world, player, current, state, null);
            DropTracker.mark(current);
            world.breakBlock(current, false);
            current = current.up();
        }
    }

    public static void breakChorus(ServerWorld world, PlayerEntity player, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        Stack<BlockPos> stack = new Stack<>();
        stack.push(start);

        while (!stack.isEmpty()) {
            BlockPos pos = stack.pop();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (state.getBlock() != Blocks.CHORUS_PLANT) continue;

            BlockPos above = pos.up();
            stack.push(above);

            for (Direction d : Direction.Type.HORIZONTAL) {
                stack.push(above.offset(d));
            }

            DropUtils.giveDrops(world, player, pos, state, null);
            DropTracker.mark(pos);
            world.breakBlock(pos, false);
        }
    }
}
