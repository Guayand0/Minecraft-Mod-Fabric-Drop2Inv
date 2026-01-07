package com.guayand0.blocks.utils;

import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;

public class TreeUtils {

    public static boolean isLog(BlockState state) {
        return state.isIn(BlockTags.LOGS);
    }
    public static boolean isLeave(BlockState state) {
        return state.isIn(BlockTags.LEAVES);
    }
}
