package com.communism.hungrystalin.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;

public class MoveToCropGoal extends MoveToBlockGoal {
    private int maxStayTicks;
    private static final int STAY_TICKS = 1200;
    private static final int GIVE_UP_TICKS = 600;

    public MoveToCropGoal(PathfinderMob mob) {
        super(mob, 1.0, 100);
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return levelReader.getBlockState(blockPos).getBlock() instanceof CropBlock;
    }

    @Override
    public boolean shouldRecalculatePath() {
        return this.tryTicks % 10 == 0;
    }

    @Override
    public void start() {
        this.moveMobToBlock();
        this.tryTicks = 0;
        this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(STAY_TICKS) + STAY_TICKS) + STAY_TICKS;
    }

    @Override
    public boolean canContinueToUse() {
        return this.tryTicks >= - this.maxStayTicks && this.tryTicks <= GIVE_UP_TICKS && this.isValidTarget(this.mob.level, this.blockPos);
    }
}
