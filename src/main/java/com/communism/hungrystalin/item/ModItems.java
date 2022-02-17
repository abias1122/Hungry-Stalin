package com.communism.hungrystalin.item;

import com.communism.hungrystalin.Main;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MOD_ID);

    public static final RegistryObject<Item> COMICALLY_LARGE_SPOON = ITEMS.register("comically_large_spoon",
            () -> new ComicallyLargeSpoonItem(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS)));

    public static final RegistryObject<ForgeSpawnEggItem> STALIN_SPAWN_EGG = ITEMS.register("stalin_spawn_egg",
            () -> new StalinSpawnEgg(new Item.Properties().tab(CreativeModeTab.TAB_MISC)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
