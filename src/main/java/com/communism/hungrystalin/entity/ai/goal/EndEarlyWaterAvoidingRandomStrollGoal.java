package com.communism.hungrystalin.entity.ai.goal;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

import java.util.function.Predicate;

public class EndEarlyWaterAvoidingRandomStrollGoal extends WaterAvoidingRandomStrollGoal {
    private final Predicate<PathfinderMob> shouldEndEarlyPredicate;
    private boolean shouldEndEarly;
    private int ticks;
    private final int TICKS_TO_CHECK_SHOULD_END_EARLY = 200;

    public EndEarlyWaterAvoidingRandomStrollGoal(PathfinderMob mob, double speedModifier, float probability, Predicate<PathfinderMob> shouldEndEarly) {
        super(mob, speedModifier, probability);
        this.shouldEndEarlyPredicate = shouldEndEarly;
    }

    @Override
    public void start() {
        super.start();
        ticks = 0;
        shouldEndEarly = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !shouldEndEarly && super.canContinueToUse();
    }

    @Override
    public void tick() {
        if (ticks % TICKS_TO_CHECK_SHOULD_END_EARLY == 0) {
            shouldEndEarly = shouldEndEarlyPredicate.test(mob);
        }

        if (!shouldEndEarly) {
            super.tick();
        }

        ++ticks;
    }
}
