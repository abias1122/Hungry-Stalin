package com.communism.hungrystalin.entity;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.ModSounds;
import com.communism.hungrystalin.Utils;
import com.communism.hungrystalin.entity.ai.goal.EatCropGoal;
import com.communism.hungrystalin.entity.ai.goal.EndEarlyWaterAvoidingRandomStrollGoal;
import com.communism.hungrystalin.entity.ai.goal.MoveToCropGoal;
import com.communism.hungrystalin.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class Stalin extends Monster {
    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(Main.MOD_ID, "entities/stalin");

    public Stalin(EntityType<? extends Stalin> entityType, Level level) {
        super(entityType, level);
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

        return Monster.checkAnyLightMonsterSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, random) && notUndergroundOrIndoors && Utils.nearbyChunkHasCrop(serverLevelAccessor, blockPos);
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
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MoveToCropGoal(this));
        this.goalSelector.addGoal(2, new EatCropGoal(this));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(15, new EndEarlyWaterAvoidingRandomStrollGoal(this, 1.0, 0f, blockPos -> Utils.nearbyChunkHasCrop(level, blockPos)));
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1d, false));
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.getTarget() != null) {
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
}
