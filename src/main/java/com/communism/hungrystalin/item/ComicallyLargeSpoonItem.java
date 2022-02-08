package com.communism.hungrystalin.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;

public class ComicallyLargeSpoonItem extends DiggerItem {
    public ComicallyLargeSpoonItem(Properties properties) {
        super(1.5f, -3.0f, Tiers.IRON, BlockTags.MINEABLE_WITH_SHOVEL, properties);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity attacker) {
        if (target instanceof Player) {
            ((Player) target).causeFoodExhaustion(5f);
        }
        return super.hurtEnemy(itemStack, attacker, target);
    }
}
