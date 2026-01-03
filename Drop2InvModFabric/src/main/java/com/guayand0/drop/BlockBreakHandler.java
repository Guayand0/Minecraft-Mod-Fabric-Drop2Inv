package com.guayand0.drop;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;

public class BlockBreakHandler {

    public static void register() {

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient() || !(world instanceof ServerWorld serverWorld)) return true;
            if (player.getAbilities().creativeMode) return true;

            Block block = state.getBlock();

            if (TreeBreakUtils.isLog(state)) {

                ItemStack held = player.getMainHandStack();
                if (!(held.getItem() instanceof AxeItem)) return true;

                TreeBreakUtils.breakTree(serverWorld, player, pos);
                return false;
            }

            if (block == Blocks.CHORUS_PLANT) {
                VerticalBreakUtils.breakChorus(serverWorld, player, pos);
                return false;
            }

            if (VerticalBreakUtils.isVerticalBlock(block)) {
                VerticalBreakUtils.breakVertical(serverWorld, player, pos, block);
                return false;
            }

            return true;
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient() || !(world instanceof ServerWorld serverWorld)) return;
            if (player.getAbilities().creativeMode) return;

            if (!VerticalBreakUtils.isVerticalBlock(state.getBlock())) {
                DropUtils.giveDrops(serverWorld, player, pos, state, blockEntity);
                DropTracker.mark(pos);
            }
        });
    }
}
