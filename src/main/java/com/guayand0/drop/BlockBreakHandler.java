package com.guayand0.drop;

import com.guayand0.utils.CropUtils;
import com.guayand0.utils.DropUtils;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.block.CropBlock;
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

            if (block instanceof CropBlock) {
                CropUtils.giveCropDrops(serverWorld, player, pos, state, blockEntity);
                serverWorld.breakBlock(pos, false); // rompe sin drops
                DropTracker.mark(pos);
                return false;
            }

            if (TreeBreakHandler.isLog(state)) {

                ItemStack held = player.getMainHandStack();
                if (!(held.getItem() instanceof AxeItem)) return true;

                TreeBreakHandler.breakTree(serverWorld, player, pos);
                return false;
            }

            if (block == Blocks.CHORUS_PLANT) {
                VerticalBreakHandler.breakChorus(serverWorld, player, pos);
                return false;
            }

            if (VerticalBreakHandler.isVerticalBlock(block)) {
                VerticalBreakHandler.breakVertical(serverWorld, player, pos, block);
                return false;
            }

            return true;
        });

        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient() || !(world instanceof ServerWorld serverWorld)) return;
            if (player.getAbilities().creativeMode) return;

            if (state.getBlock() instanceof CropBlock) return;

            if (!VerticalBreakHandler.isVerticalBlock(state.getBlock())) {
                DropUtils.giveDrops(serverWorld, player, pos, state, blockEntity);
                DropTracker.mark(pos);
            }
        });
    }
}
