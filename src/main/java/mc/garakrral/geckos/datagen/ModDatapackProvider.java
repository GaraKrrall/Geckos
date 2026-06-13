/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

/**
 * Placeholder source file for a future built-in datapack provider.
 *
 * <p>The previous implementation is currently commented out, but the file remains to preserve the
 * intended integration point for worldgen or builtin-datapack generation when those systems return.
 */
package mc.garakrral.geckos.datagen;

/*import java.util.Set;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.worldgen.ModBiomeModifiers;
import mc.garakrral.geckos.worldgen.ModConfiguredFeatures;
import mc.garakrral.geckos.worldgen.ModPlacedFeatures;

import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModDatapackProvider extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap)
            .add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap);

    public ModDatapackProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Geckos.MODID));
    }
}*/
