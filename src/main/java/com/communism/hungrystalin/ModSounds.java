package com.communism.hungrystalin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    private ModSounds() {
    }

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, HungryStalin.MOD_ID);

    public static final RegistryObject<SoundEvent> KULAK = SOUNDS.register(
            "entity.stalin.kulak",
            () -> new SoundEvent(new ResourceLocation(HungryStalin.MOD_ID, "entity.stalin.kulak")));

    public static final RegistryObject<SoundEvent> BURP = SOUNDS.register(
            "entity.stalin.burp",
            () -> new SoundEvent(new ResourceLocation(HungryStalin.MOD_ID, "entity.stalin.burp")));

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
