package com.guayand0;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class BlockBreakHandler {

    public static void register() {

        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClientSide() || !(world instanceof ServerLevel serverWorld)) return true;
            if (player.getAbilities().instabuild) return true;

            Block block = state.getBlock();

            if (TreeBreakUtils.isLog(state)) {

                ItemStack held = player.getMainHandItem();
                if (!(held.getItem() instanceof AxeItem)) {
                    return true; // sin hacha: rompe solo el bloque normal
                }

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
            if (world.isClientSide() || !(world instanceof ServerLevel serverWorld)) return;
            if (player.getAbilities().instabuild) return;

            if (!VerticalBreakUtils.isVerticalBlock(state.getBlock())) {
                DropUtils.giveDrops(serverWorld, player, pos, state, blockEntity);
                DropTracker.mark(pos);
            }
        });
    }
}
