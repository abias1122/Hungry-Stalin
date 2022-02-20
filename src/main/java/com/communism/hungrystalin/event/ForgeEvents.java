package com.communism.hungrystalin.event;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.entity.ModEntities;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {
    private ForgeEvents() {
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void biomeLoading(BiomeLoadingEvent event) {
        Biome.BiomeCategory category = event.getCategory();

        if (category != Biome.BiomeCategory.NETHER && category != Biome.BiomeCategory.THEEND) {
            event.getSpawns().addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntities.STALIN.get(), 100, 1, 1));
        }
    }
}
