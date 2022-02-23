package com.communism.hungrystalin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;

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

    public static boolean blockPosIsCrop(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos).getBlock() instanceof CropBlock;
    }

    @NotNull
    public static List<BlockPos> getSurroundingBlockPoses(Mob mob) {
        BlockPos[] blockPosArr = new BlockPos[] {
                mob.blockPosition(),
                mob.blockPosition().north(),
                mob.blockPosition().south(),
                mob.blockPosition().east(),
                mob.blockPosition().west(),
                mob.blockPosition().north().east(),
                mob.blockPosition().north().west(),
                mob.blockPosition().south().east(),
                mob.blockPosition().south().west()
        };

        ArrayList<BlockPos> blockPoses = new ArrayList<>(Arrays.asList(blockPosArr));

        for (BlockPos blockPos : blockPosArr) {
            blockPoses.add(blockPos.above());
        }

        return blockPoses;
    }

    public static boolean nearbyChunkHasCrop(LevelAccessor levelAccessor, BlockPos blockPos) {
        return Stream.of(
                getBlocksFromChunk(levelAccessor.getChunk(blockPos)),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.east(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.west(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(16).east(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(16).west(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(16).east(16))),
                getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(16).west(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(32).east(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(32).east(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(32).west(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.north(32).west(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(32).east(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(32).east(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(32).west(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.south(32).west(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.east(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.east(32).north(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.east(32).south(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.west(32))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.west(32).north(16))),
                Utils.getBlocksFromChunk(levelAccessor.getChunk(blockPos.west(32).south(16)))
        ).flatMap(stream -> stream).anyMatch(block -> block instanceof CropBlock);
    }

    public static boolean nearbyBlockHasCrop(Mob mob) {
        return getSurroundingBlockPoses(mob).stream()
                .anyMatch(blockPos -> blockPosIsCrop(mob.level, blockPos));
    }
}
