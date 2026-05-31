/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.item.group;

import java.util.function.Supplier;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.item.ModItems;

import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItemGroups {
    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Geckos.MODID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = TABS.register("main",
            () -> CreativeModeTab.builder().icon(
                            () -> new ItemStack(ModItems.GECKO_SPAWN_EGG.get()))
                    .title(Component.literal("Geckos"))
                    .displayItems((itemDisplayParameters, output) -> {
                        output.accept(ModItems.GECKO_SPAWN_EGG);
                        output.accept(ModItems.FLY_SPAWN_EGG);
                        output.accept(ModItems.DEAD_FLY);
                    })
                    .build());
}
