package com.communism.hungrystalin;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Utils {
    private Utils() {
    }

    public static Stream<Block> getBlocksFromChunk(ChunkAccess chunk) {
        if (chunk instanceof LevelChunk levelChunk) {
            List<Block> blocks = new ArrayList<>();
            Arrays.stream(levelChunk.getSections()).forEach(section -> section.getStates().getAll(blockState -> blocks.add(blockState.getBlock())));
            return blocks.stream();
        }
        else {
            return Stream.empty();
        }
    }
}
