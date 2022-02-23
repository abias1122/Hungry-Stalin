package com.communism.hungrystalin.entity.ai.goal;

import com.communism.hungrystalin.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CropBlock;

public class MoveToCropGoal extends MoveToBlockGoal {
    private static final int GIVE_UP_TICKS = 600;
    private boolean shouldStop;
    private boolean isNearCrop;
    private int timesRun;

    public MoveToCropGoal(PathfinderMob mob) {
        super(mob, 1.0, 100, 20);
        timesRun = 0;
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
        super.start();
        shouldStop = false;
        isNearCrop = true;
        ++timesRun;
    }

    @Override
    protected int nextStartTick(PathfinderMob pathfinderMob) {
        return (isNearCrop || timesRun == 0) ? 3 : super.nextStartTick(pathfinderMob);
    }

    @Override
    public void stop() {
        shouldStop = true;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    protected boolean isReachedTarget() {
        return super.isReachedTarget() || Utils.nearbyBlockHasCrop(mob);
    }

    @Override
    public boolean canContinueToUse() {
        boolean canContinue = !shouldStop && !this.isReachedTarget() && tryTicks <= GIVE_UP_TICKS && isValidTarget(mob.level, blockPos);
        if (!canContinue) {
            isNearCrop = findNearestBlock();
        }
        return canContinue;
    }
}
