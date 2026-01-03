package com.guayand0.drop;

import net.minecraft.util.math.BlockPos;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DropTracker {

    private static final Set<BlockPos> BROKEN = ConcurrentHashMap.newKeySet();

    public static void mark(BlockPos pos) {
        BROKEN.add(pos.toImmutable());
    }

    public static boolean consume(BlockPos pos) {
        return BROKEN.remove(pos);
    }
}
