package com.communism.hungrystalin.event;

import com.communism.hungrystalin.Main;
import com.communism.hungrystalin.entity.ModEntities;
import com.communism.hungrystalin.entity.Stalin;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Main.MOD_ID, bus = Bus.MOD)
public class ModEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STALIN.get(), Stalin.createAttributes().build());
    }
}
