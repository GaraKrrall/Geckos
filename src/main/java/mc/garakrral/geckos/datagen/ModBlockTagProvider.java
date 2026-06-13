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
import net.minecraft.tags.BlockTags;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.block.ModBlocks;

import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * Generates block tag data for the mod.
 */
public class ModBlockTagProvider extends BlockTagsProvider {
    /**
     * Creates the block tag provider.
     *
     * @param output pack output target
     * @param lookupProvider registry lookup future
     * @param existingFileHelper helper for existing generated resources
     */
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Geckos.MODID, existingFileHelper);
    }

    /**
     * Adds block tags to the generated data set.
     *
     * @param provider resolved holder lookup provider
     */
    @Override
    protected void addTags(HolderLookup.Provider provider) {}
}
