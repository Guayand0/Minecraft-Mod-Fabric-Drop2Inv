package com.guayand0.blocks;

import com.guayand0.blocks.utils.MushroomUtils;
import com.guayand0.blocks.utils.DropUtils;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

import java.util.*;

public class MushroomCapBreakHandler {

    public static void breakCaps(ServerWorld world, PlayerEntity player, Set<BlockPos> stems) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        List<BlockPos> capCandidates = new ArrayList<>();

        // 🔍 Detectar tipo de seta usando la capucha sobre el tallo
        Boolean isRedMushroom = null;
        for (BlockPos stem : stems) {
            BlockState above = world.getBlockState(stem.up());
            if (MushroomUtils.isMushroomCap(above.getBlock())) {
                isRedMushroom = MushroomUtils.isRed(above.getBlock());
                break;
            }
        }

        if (isRedMushroom == null) return;

        // Añadir bloques adyacentes a los tallos
        for (BlockPos stem : stems) {
            for (BlockPos adj : getAdjacent6(stem)) {
                if (!visited.contains(adj)) queue.add(adj);
            }
        }

        // BFS para recorrer capuchas
        while (!queue.isEmpty()) {
            BlockPos pos = queue.poll();
            if (!visited.add(pos)) continue;

            BlockState state = world.getBlockState(pos);
            if (!MushroomUtils.isMushroomCap(state.getBlock())) continue;

            // ⛔ Bloquear mezcla rojo / marrón
            if (isRedMushroom && !MushroomUtils.isRed(state.getBlock())) continue;
            if (!isRedMushroom && !MushroomUtils.isBrown(state.getBlock())) continue;

            capCandidates.add(pos);

            // Expandir BFS según tipo
            List<BlockPos> nextAdj = isRedMushroom
                    ? getAdjacentWithDiagonals(pos)   // rojas
                    : getAdjacent6(pos);              // marrones

            for (BlockPos adj : nextAdj) {
                if (!visited.contains(adj)) queue.add(adj);
            }
        }

        // Romper capuchas dentro del radio seguro
        for (BlockPos cap : capCandidates) {
            int minDistToStem = stems.stream()
                    .mapToInt(stem -> manhattan(stem, cap))
                    .min().orElse(Integer.MAX_VALUE);

            if (minDistToStem <= 7) {
                BlockState state = world.getBlockState(cap);
                DropUtils.giveDrops(world, player, cap, state, null);
                world.breakBlock(cap, false);
            }
        }
    }

    // 6 adyacentes
    private static List<BlockPos> getAdjacent6(BlockPos pos) {
        return List.of(pos.up(), pos.down(), pos.north(), pos.south(), pos.east(), pos.west());
    }

    // 6 adyacentes + diagonales planas
    private static List<BlockPos> getAdjacentWithDiagonals(BlockPos pos) {
        List<BlockPos> adjacents = new ArrayList<>(getAdjacent6(pos));

        int[] offsets = {-1, 0, 1};
        for (int dx : offsets) {
            for (int dy : offsets) {
                for (int dz : offsets) {
                    if (dx == 0 && dy == 0 && dz == 0) continue;
                    int axesChanged = (dx != 0 ? 1 : 0)
                            + (dy != 0 ? 1 : 0)
                            + (dz != 0 ? 1 : 0);
                    if (axesChanged == 2) {
                        adjacents.add(pos.add(dx, dy, dz));
                    }
                }
            }
        }

        return adjacents;
    }

    private static int manhattan(BlockPos a, BlockPos b) {
        return Math.abs(a.getX() - b.getX())
                + Math.abs(a.getY() - b.getY())
                + Math.abs(a.getZ() - b.getZ());
    }
}
