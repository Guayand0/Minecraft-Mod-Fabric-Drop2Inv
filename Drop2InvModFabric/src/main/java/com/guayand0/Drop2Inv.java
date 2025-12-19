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
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {

			if (world.isClientSide()) return true;

			if (isVerticalBlock(state.getBlock())) {
				handleVerticalBreak(world, player, pos, state);
			} else {
				handleSingleBreak(world, player, pos, state, blockEntity);
			}

			return false;
		});
	}

	private boolean isVerticalBlock(Block block) {
		return block == Blocks.SUGAR_CANE
				|| block == Blocks.CACTUS
				|| block == Blocks.BAMBOO;
	}

	private void handleSingleBreak(
			Level world,
			Player player,
			BlockPos pos,
			BlockState state,
			@Nullable BlockEntity blockEntity
	) {
		ServerLevel serverWorld = (ServerLevel) world;
		ItemStack tool = player.getMainHandItem();

		List<ItemStack> drops = Block.getDrops(
				state, serverWorld, pos, blockEntity, player, tool
		);

		giveDrops(player, drops, world, pos, state, blockEntity);
		damageToolOnce(player);
		world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
	}

	private void handleVerticalBreak(Level world, Player player, BlockPos startPos, BlockState baseState) {
		ServerLevel serverWorld = (ServerLevel) world;
		ItemStack tool = player.getMainHandItem();
		Block baseBlock = baseState.getBlock();

		BlockPos currentPos = startPos;
		boolean damaged = false;
		List<BlockPos> blocksToBreak = new ArrayList<>();

		while (true) {
			BlockState currentState = world.getBlockState(currentPos);
			Block currentBlock = currentState.getBlock();

			// Si no es el mismo tipo de bloque, salir
			if (currentBlock != baseBlock) break;

			blocksToBreak.add(currentPos);
			currentPos = currentPos.above();
		}

		// Ahora romper todos los bloques guardados
		for (BlockPos pos : blocksToBreak) {
			BlockState state = world.getBlockState(pos);
			List<ItemStack> drops = Block.getDrops(state, serverWorld, pos, null, player, tool);
			giveDrops(player, drops, world, pos, state, null);
			world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

			if (!damaged) {
				damageToolOnce(player);
				damaged = true;
			}
		}
	}

	private void giveDrops(Player player, List<ItemStack> drops, Level world, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
		for (ItemStack stack : drops) {
			// Clonar stack para manejar cantidades parciales
			ItemStack remaining = stack.copy();
			while (!remaining.isEmpty()) {
				boolean added = player.getInventory().add(remaining);
				if (added) break; // Se agregó al inventario

				// Si no cabe, soltar en el mundo
				Block.dropResources(state, world, pos, blockEntity, player, remaining);
				break;
			}
		}
	}

	private void damageToolOnce(Player player) {
		ItemStack tool = player.getMainHandItem();

		if (tool.isEmpty() || !tool.isDamageableItem()) return;

		tool.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
	}

}