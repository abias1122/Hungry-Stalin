package com.communism.hungrystalin.entity.ai.goal;

import com.communism.hungrystalin.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.EatBlockGoal;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.List;

public class EatCropGoal extends EatBlockGoal {
    protected final Mob mob;
    protected final Level level;
    private final int MAX_TICKS = 10;
    private int ticks;
    private boolean hasEaten;

    public EatCropGoal(Mob mob) {
        super(mob);
        this.mob = mob;
        this.level = mob.level;
    }

    @Override
    public boolean canUse() {
        return Utils.nearbyBlockHasCrop(mob);
    }

    @Override
    public boolean canContinueToUse() {
        return !this.hasEaten && this.ticks < MAX_TICKS;
    }

    @Override
    public void start() {
        super.start();
        this.ticks = 0;
        this.hasEaten = false;
    }

    @Override
    public boolean isInterruptable() {
        return false;
    }

    @Override
    public void stop() {
        this.hasEaten = true;
    }

    @Override
    public void tick() {
        ++this.ticks;
        if (this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            List<BlockPos> cropsToEat = Utils.getSurroundingBlockPoses(this.mob).stream()
                    .filter(blockPos -> Utils.blockPosIsCrop(this.level, blockPos)).toList();
            for (BlockPos cropToEat : cropsToEat) {
                this.level.destroyBlock(cropToEat, false);
                this.mob.ate();
                this.mob.gameEvent(GameEvent.EAT, this.mob.eyeBlockPosition());
            }
            if (cropsToEat.size() > 0) {
                this.hasEaten = true;
            }
        }
    }
}
