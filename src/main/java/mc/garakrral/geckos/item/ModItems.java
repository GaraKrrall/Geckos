package mc.garakrral.geckos.item;

import net.minecraft.world.item.Item;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.item.feature.SimpleItem;

import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Main.MODID);

    public static final DeferredItem<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GECKO, 0x31afaf, 0xffac00,
                    new Item.Properties()));

    public static final DeferredItem<Item> FLY_SPAWN_EGG = ITEMS.register("fly_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FLY, 0x34adfa, 0xffaa00,
                    new Item.Properties()));

    public static final DeferredItem<Item> DEAD_FLY = ITEMS.register("dead_fly",
            () -> new SimpleItem(new Item.Properties()));

}
