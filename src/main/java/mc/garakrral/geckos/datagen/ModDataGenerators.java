/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.datagen;

import java.util.Collections;
import java.util.List;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import mc.garakrral.geckos.Geckos;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

/**
 * Entry point for data generation provider registration.
 *
 * <p>This subscriber wires the project's recipe, loot, model, and tag generators into NeoForge's
 * gather-data lifecycle.
 */
@SuppressWarnings("removal")
@EventBusSubscriber(modid = Geckos.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDataGenerators {

    /**
     * Registers all active data providers based on the include-client/include-server flags.
     *
     * @param dataEvent gather-data event carrying output paths and registry lookups
     */
    @SubscribeEvent
    public static void gatherData(GatherDataEvent dataEvent) {
        DataGenerator gen = dataEvent.getGenerator();
        PackOutput out = gen.getPackOutput();
        ExistingFileHelper helper = dataEvent.getExistingFileHelper();
        var lookup = dataEvent.getLookupProvider();
        BlockTagsProvider modBlockTagProvider = new ModBlockTagProvider(out, lookup, helper);

        gen.addProvider(dataEvent.includeServer(), new LootTableProvider(out, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookup));

        gen.addProvider(dataEvent.includeServer(), new ModRecipeProvider(out, lookup));

        gen.addProvider(dataEvent.includeClient(), new ModBlockStateProvider(out, helper));

        gen.addProvider(dataEvent.includeServer(), modBlockTagProvider);

        gen.addProvider(dataEvent.includeServer(), new ModItemTagProvider(out, lookup, modBlockTagProvider.contentsGetter(), helper));

        //gen.addProvider(dataEvent.includeServer(), new ModDatapackProvider(out, lookup));

        gen.addProvider(dataEvent.includeClient(), new ModItemModelProvider(out, helper));
    }
}
