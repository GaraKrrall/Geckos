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
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import net.neoforged.neoforge.common.conditions.IConditionBuilder;

/**
 * Generates crafting recipes for the mod.
 */
public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    /**
     * Creates the recipe provider.
     *
     * @param output pack output target
     * @param registries registry lookup future
     */
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    /**
     * Adds recipe definitions to the generated data set.
     *
     * @param recipeOutput recipe output sink used by the provider
     */
    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {

    }

}
