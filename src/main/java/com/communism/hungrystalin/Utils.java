package com.communism.hungrystalin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.stream.Stream;

public class Utils {
    private Utils() {
    }

    public static Stream<Block> GetBlocksFromChunk(LevelChunk chunk) {
        return chunk.getBlockEntities().values().stream().map(entity -> entity.getBlockState().getBlock());
    }
}
