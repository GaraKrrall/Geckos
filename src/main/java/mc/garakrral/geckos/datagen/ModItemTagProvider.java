/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;

import mc.garakrral.geckos.Geckos;

import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Generates item tag data for the mod.
 */
public class ModItemTagProvider extends ItemTagsProvider {
    /**
     * Creates the item tag provider.
     *
     * @param output pack output target
     * @param lookupProvider registry lookup future
     * @param blockTags future block tag lookup used for tag copying
     * @param existingFileHelper helper for existing generated resources
     */
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Geckos.MODID, existingFileHelper);
    }

    /**
     * Adds item tags to the generated data set.
     *
     * @param provider resolved holder lookup provider
     */
    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {

    }
}
