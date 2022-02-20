package com.communism.hungrystalin.entity;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.ModSounds;
import com.communism.hungrystalin.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;
import java.util.stream.Stream;

public class Stalin extends Monster {
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(Main.MOD_ID, "entities/stalin");

    public Stalin(EntityType<? extends Stalin> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30d).add(Attributes.MOVEMENT_SPEED, 0.25d);
    }

    public static boolean checkStalinSpawnRules(EntityType<? extends Stalin> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, Random random) {
        boolean nearbyChunkHasCrop = Stream.of(
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos)),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(16).north(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(16).south(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(16).north(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(16).south(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(32).east(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(32).east(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(32).west(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.north(32).west(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(32).east(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(32).east(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(32).west(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.south(32).west(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(32).north(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.east(32).south(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(32))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(32).north(16))),
                Utils.getBlocksFromChunk(serverLevelAccessor.getChunk(blockPos.west(32).south(16)))
        ).flatMap(stream -> stream).anyMatch(block -> block instanceof CropBlock);

        boolean notUndergroundOrIndoors = true;
        for (int y = blockPos.getY() + 1; y < 320; ++y) {
                BlockState blockState = serverLevelAccessor.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ()));
                Block block = blockState.getBlock();
                if (!(block instanceof AirBlock || block instanceof LeavesBlock)) {
                    notUndergroundOrIndoors = false;
                    break;
                }
        }

        return Monster.checkAnyLightMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, random) && notUndergroundOrIndoors && nearbyChunkHasCrop;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0, 0f));
        this.goalSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.KULAK.get();
    }
}
