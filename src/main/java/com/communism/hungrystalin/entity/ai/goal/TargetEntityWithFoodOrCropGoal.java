package com.communism.hungrystalin.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class TargetEntityWithFoodOrCropGoal extends NearestAttackableTargetGoal<LivingEntity> {
    public TargetEntityWithFoodOrCropGoal(Mob mob, @Nullable Predicate<LivingEntity> predicate) {
        super(mob, LivingEntity.class, true, predicate);
        targetConditions = TargetingConditions.forCombat().range(this.getFollowDistance()).selector(this::livingEntityIsCarryingFood);
    }

    private boolean livingEntityIsCarryingFood(LivingEntity livingEntity) {
        ItemStack mainHandItem = livingEntity.getMainHandItem();
        if (!mainHandItem.isEmpty()) {
            Item item = mainHandItem.getItem();

            return item.getCreativeTabs().stream().anyMatch(tab -> tab.equals(CreativeModeTab.TAB_FOOD)) ||
                    item.getTags().stream().anyMatch(tag -> tag.toString().equals("forge:crops"));
        }

        return false;
    }
}
