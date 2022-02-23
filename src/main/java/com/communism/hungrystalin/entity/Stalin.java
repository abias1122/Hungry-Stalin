package com.communism.hungrystalin.entity;

import com.communism.hungrystalin.HungryStalin;
import com.communism.hungrystalin.ModSounds;
import com.communism.hungrystalin.Utils;
import com.communism.hungrystalin.entity.ai.goal.EatCropGoal;
import com.communism.hungrystalin.entity.ai.goal.MoveToCropGoal;
import com.communism.hungrystalin.entity.ai.goal.TargetEntityWithFoodOrCropGoal;
import com.communism.hungrystalin.entity.ai.goal.UninterruptibleAfterNIInterruptsWaterAvoidingRandomStrollGoal;
import com.communism.hungrystalin.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.UUID;

public class Stalin extends PathfinderMob implements NeutralMob {
    private int remainingPersistentAngerTime;
    private UUID persistentAngerTarget;
    private int burpTicks;
    private final int MAX_BURP_TICKS = 10;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(HungryStalin.MOD_ID, "entities/stalin");

    public Stalin(EntityType<? extends Stalin> entityType, Level level) {
        super(entityType, level);
        burpTicks = MAX_BURP_TICKS;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30d).add(Attributes.MOVEMENT_SPEED, 0.25d).add(Attributes.ATTACK_DAMAGE, 1.0).add(Attributes.ATTACK_KNOCKBACK);
    }

    public static boolean checkStalinSpawnRules(EntityType<? extends Stalin> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, Random random) {

        boolean notUndergroundOrIndoors = true;
        for (int y = blockPos.getY() + 1; y < 320; ++y) {
                BlockState blockState = serverLevelAccessor.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ()));
                Block block = blockState.getBlock();
                if (!(block instanceof AirBlock || block instanceof LeavesBlock)) {
                    notUndergroundOrIndoors = false;
                    break;
                }
        }

        return serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && checkMobSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, random) && notUndergroundOrIndoors && Utils.nearbyChunkHasCrop(serverLevelAccessor, blockPos);
    }

    @Override
    public boolean doHurtTarget(Entity target) {
        boolean theHurt =  super.doHurtTarget(target);
        ItemStack mainHandItem = this.getMainHandItem();
        if (!mainHandItem.isEmpty() && target instanceof LivingEntity livingTarget) {
            mainHandItem.getItem().hurtEnemy(mainHandItem, livingTarget, this);
        }
        return theHurt;
    }

    @Override
    public void ate() {
        super.ate();
        burpTicks = 0;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(2, new MoveToCropGoal(this));
        goalSelector.addGoal(2, new EatCropGoal(this));
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(15, new UninterruptibleAfterNIInterruptsWaterAvoidingRandomStrollGoal(this, 1.0, 0f));
        goalSelector.addGoal(1, new HurtByTargetGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1d, false));
        goalSelector.addGoal(3, new TargetEntityWithFoodOrCropGoal(this, this::isAngryAt));
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (burpTicks < MAX_BURP_TICKS) {
            return ModSounds.BURP.get();
        }
        else if (this.getTarget() != null) {
            return ModSounds.KULAK.get();
        }
        else {
            return null;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.COMICALLY_LARGE_SPOON.get()));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData spawnGroupDataToReturn = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        this.populateDefaultEquipmentSlots(difficultyInstance);
        return spawnGroupDataToReturn;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        updatePersistentAnger((ServerLevel)level, true);
        ++burpTicks;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    public void startPersistentAngerTimer() {
        setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(random));
    }
}
