package com.guayand0;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Drop2Inv implements ModInitializer {

	public static final String MOD_ID = "drop2inv";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		dropBlocks();
	}

	private void dropBlocks() {
		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (world.isClientSide()) return;

			// Creativo → vanilla puro
			if (player.isCreative()) return;

			// Survival → lógica custom
			if (isVerticalBlock(state.getBlock())) {
				handleVerticalBreak(world, player, pos, state);
			} else {
				handleSingleBreak(world, player, pos, state, blockEntity);
			}
		});
	}

	private boolean isVerticalBlock(Block block) {
		return block == Blocks.SUGAR_CANE || block == Blocks.CACTUS || block == Blocks.BAMBOO || block == Blocks.KELP_PLANT;
	}

	private void handleSingleBreak(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		ServerLevel serverWorld = (ServerLevel) world;
		ItemStack tool = player.getMainHandItem();

		List<ItemStack> drops = Block.getDrops(state, serverWorld, pos, blockEntity, player, tool);
		giveDrops(player, drops);
		damageToolOnce(player);
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
	}

	private void handleVerticalBreak(Level world, Player player, BlockPos startPos, BlockState baseState) {
		ServerLevel serverWorld = (ServerLevel) world;
		ItemStack tool = player.getMainHandItem();
		Block baseBlock = baseState.getBlock();

		BlockPos currentPos = startPos;
		boolean damaged = false;

		while (true) {
			BlockState currentState = world.getBlockState(currentPos);
			Block currentBlock = currentState.getBlock();

			if (baseBlock == Blocks.KELP_PLANT) {
				if (currentBlock != Blocks.KELP_PLANT && currentBlock != Blocks.KELP) break;
			} else {
				if (currentBlock != baseBlock) break;
			}

			List<ItemStack> drops = Block.getDrops(currentState, serverWorld, currentPos, null, player, tool);
			giveDrops(player, drops);

			if (currentBlock == Blocks.KELP_PLANT || currentBlock == Blocks.KELP) {
				world.setBlock(currentPos, Blocks.WATER.defaultBlockState(), 3);
			} else {
				world.setBlock(currentPos, Blocks.AIR.defaultBlockState(), 3);
			}

			if (!damaged) {
				damageToolOnce(player);
				damaged = true;
			}

			currentPos = currentPos.above();
		}
	}

	private void giveDrops(Player player, List<ItemStack> drops) {
		for (ItemStack stack : drops) {
			ItemStack remaining = stack.copy();
			player.getInventory().add(remaining); // añadimos directamente al inventario
		}
	}

	private void damageToolOnce(Player player) {
		ItemStack tool = player.getMainHandItem();
		if (tool.isEmpty() || !tool.isDamageableItem()) return;
		tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
	}

}