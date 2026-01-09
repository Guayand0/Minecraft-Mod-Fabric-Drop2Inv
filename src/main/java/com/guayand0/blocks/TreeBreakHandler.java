package com.guayand0.blocks;

import com.guayand0.config.Drop2InvConfig;
import com.guayand0.blocks.utils.DropUtils;
import com.guayand0.blocks.utils.TreeUtils;
import com.guayand0.config.Drop2InvConfigManager;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.block.BlockState;

import java.util.*;

public class TreeBreakHandler {

    public static void breakTree(ServerWorld world, PlayerEntity player, BlockPos start) {

        if (!(player instanceof ServerPlayerEntity serverPlayer)) return;

        Drop2InvConfig config = Drop2InvConfigManager.get();
        ItemStack axe = player.getMainHandStack();

        Set<BlockPos> visited = new HashSet<>();
        Set<BlockPos> logs = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(start);

        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (!TreeUtils.isLog(state)) continue;

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
                    world.breakBlock(pos, false);

                    axe.damage(1, serverPlayer, EquipmentSlot.MAINHAND);
                });


        if (config.blocks.break_tree_leaf) {
            LeafBreakHandler.breakLeavesFromLogs(world, player, logs);
        }
    }
}
