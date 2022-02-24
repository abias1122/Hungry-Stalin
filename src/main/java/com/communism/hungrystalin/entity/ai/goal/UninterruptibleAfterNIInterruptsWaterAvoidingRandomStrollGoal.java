package com.communism.hungrystalin.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class UninterruptibleAfterNIInterruptsWaterAvoidingRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
    private int interrupts;
    private int usesWithoutInterrupts;
    private static final int MAX_INTERRUPTS = 5;
    private static final int MAX_USES_WITHOUT_INTERRUPT = 10;

    public UninterruptibleAfterNIInterruptsWaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier, float probability) {
        super(mob, speedModifier, probability);
        interrupts = 0;
        usesWithoutInterrupts = 0;
    }

    @Override
    public boolean isInterruptable() {
        return interrupts < MAX_INTERRUPTS;
    }

    @Override
    public void stop() {
        super.stop();
        ++interrupts;
    }

    @Override
    public boolean canContinueToUse() {
        boolean canContinue = super.canContinueToUse();

        if (!canContinue
                && !isInterruptable()
                && usesWithoutInterrupts++ >= MAX_USES_WITHOUT_INTERRUPT) {
            interrupts = 0;
            usesWithoutInterrupts = 0;
        }

        return canContinue;
    }
}
