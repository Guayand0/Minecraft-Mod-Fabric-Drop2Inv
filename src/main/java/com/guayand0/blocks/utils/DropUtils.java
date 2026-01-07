package com.guayand0.blocks.utils;

import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DropUtils {

    public static void giveDrops(ServerWorld world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

        ItemStack tool = player.getMainHandStack();

        if (state.isToolRequired() && !tool.isSuitableFor(state)) return;

        List<ItemStack> drops =
                Block.getDroppedStacks(state, world, pos, blockEntity, player, tool);

        for (ItemStack stack : drops) {
            player.getInventory().insertStack(stack);
        }
    }
}
