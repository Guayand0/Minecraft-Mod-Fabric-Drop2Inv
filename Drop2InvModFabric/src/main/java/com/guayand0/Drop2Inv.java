package com.guayand0;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class Drop2Inv implements ModInitializer {

	public static final String MOD_ID = "drop2inv";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Posiciones de bloques rotos por jugador
	private static final Set<BlockPos> PLAYER_BROKEN_BLOCKS = ConcurrentHashMap.newKeySet();

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		registerBlockBreak();
		cancelPlayerBlockDrops();
	}

	private void registerBlockBreak() {
		PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
			if (world.isClientSide()) return true;
			if (!(world instanceof ServerLevel serverWorld)) return true;
			if (player.isCreative()) return true;

			Block block = state.getBlock();

			if (isVerticalBlock(block)) {
				cancelVerticalBlocksAbove(serverWorld, player, pos, block);
				return false; // Cancela el drop original
			}
            return true;
        });

		PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
			if (world.isClientSide()) return;
			if (!(world instanceof ServerLevel serverWorld)) return;
			if (player.isCreative()) return;

			Block block = state.getBlock();

			if (!isVerticalBlock(block)) {
				giveBlockDropsToPlayer(serverWorld, player, pos, state, blockEntity);
				PLAYER_BROKEN_BLOCKS.add(pos.immutable());
			}
		});
	}

	private boolean isVerticalBlock(Block block) {
		return block == Blocks.BAMBOO || block == Blocks.SUGAR_CANE ||
				block == Blocks.CACTUS || block == Blocks.KELP_PLANT;
	}

	private void cancelVerticalBlocksAbove(ServerLevel world, Player player, BlockPos pos, Block blockType) {
		BlockPos currentPos = pos;
		while (true) {
			BlockState stateAbove = world.getBlockState(currentPos);
			Block blockAbove = stateAbove.getBlock();

			// Caso especial para kelp
			if (blockType == Blocks.KELP_PLANT) {
				if (blockAbove != Blocks.KELP_PLANT && blockAbove != Blocks.KELP) break;
			} else {
				if (blockAbove != blockType) break;
			}

			giveBlockDropsToPlayer(world, player, currentPos, stateAbove, null); // Dar drops al jugador
			PLAYER_BROKEN_BLOCKS.add(currentPos.immutable()); // Marca para cancelar drops
			world.destroyBlock(currentPos, false); // Rompe el bloque sin drops
			currentPos = currentPos.above();
		}
	}

	private void giveBlockDropsToPlayer(ServerLevel world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {

		ItemStack tool = player.getMainHandItem();

		// Comprueba si el bloque requiere herramienta y si la herramienta actual es válida
		if (state.requiresCorrectToolForDrops() && !tool.isCorrectToolForDrops(state)) return;

		// Respeta Fortune / Silk Touch
		List<ItemStack> drops = Block.getDrops(state, world, pos, blockEntity, player, tool);

		for (ItemStack stack : drops) {
			player.getInventory().add(stack);
		}
	}

	private void cancelPlayerBlockDrops() {
		ServerEntityEvents.ENTITY_LOAD.register((entity, world) -> {
			if (!(entity instanceof ItemEntity item)) return;
			if (!(world instanceof ServerLevel)) return;

			BlockPos spawnPos = item.blockPosition();

			// Si el ítem viene de un bloque roto por jugador → eliminar
			if (PLAYER_BROKEN_BLOCKS.remove(spawnPos)) {
				item.discard();
			}
		});
	}
}
