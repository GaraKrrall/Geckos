/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.datagen;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.item.ModItems;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

/**
 * Generates item model data for the mod.
 */
public class ModItemModelProvider extends ItemModelProvider {
    /**
     * Creates the provider.
     *
     * @param output pack output target
     * @param helper helper for existing model references
     */
    public ModItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Geckos.MODID, helper);
    }

    /**
     * Registers generated item models.
     */
    @Override
    protected void registerModels() {
        basicItem(ModItems.DEAD_FLY.get());
    }
}
