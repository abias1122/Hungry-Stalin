package com.communism.hungrystalin.entity;

import com.communism.hungrystalin.HungryStalin;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    private ModEntities() {}

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, HungryStalin.MOD_ID);

    public static final RegistryObject<EntityType<Stalin>> STALIN = ENTITIES.register("stalin",
            () -> EntityType.Builder.of(Stalin::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.8f)
                    .build(new ResourceLocation(HungryStalin.MOD_ID, "stalin").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
