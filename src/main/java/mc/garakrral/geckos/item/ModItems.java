package mc.garakrral.geckos.item;

import net.minecraft.world.item.Item;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.item.feature.SimpleItem;

import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Geckos.MODID);

    public static final RegistryObject<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.GECKO, 0x31afaf, 0xffac00,
                    new Item.Properties()));

    public static final RegistryObject<Item> FLY_SPAWN_EGG = ITEMS.register("fly_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.FLY, 0x34adfa, 0xffaa00,
                    new Item.Properties()));


    public static final RegistryObject<Item> DEAD_FLY = ITEMS.register("dead_fly",
            () -> new SimpleItem(new Item.Properties()));
}
