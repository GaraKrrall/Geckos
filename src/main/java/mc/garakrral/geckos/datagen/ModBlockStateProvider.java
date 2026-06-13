/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.block.ModBlocks;

import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

/**
 * Generates blockstate and block model data for the mod.
 */
public class ModBlockStateProvider extends BlockStateProvider {
    /**
     * Creates the provider.
     *
     * @param output pack output target
     * @param exFileHelper helper for resolving existing model files
     */
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Geckos.MODID, exFileHelper);
    }

    /**
     * Registers blockstate and model definitions.
     */
    @Override
    protected void registerStatesAndModels() {
      blockItem(ModBlocks.GECKO_STATUE);
    }

    /**
     * Generates a cross-model sapling block.
     *
     * @param object deferred block handle
     */
    private void saplingBlock(DeferredBlock<Block> object) {
        simpleBlock(object.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(object.get()).getPath(), blockTexture(object.get())).renderType("cutout"));
    }

    /**
     * Generates a cutout leaves block model and item.
     *
     * @param object deferred block handle
     */
    private void leavesBlock(DeferredBlock<Block> object) {
        simpleBlockWithItem(object.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(object.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(object.get())).renderType("cutout"));
    }

    /**
     * Generates a cube-all block model and matching item model.
     *
     * @param block deferred block handle
     */
    private void blockWithItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    /**
     * Generates a simple block item model pointing at the block model.
     *
     * @param block deferred block handle
     */
    private void blockItem(DeferredBlock<?> block) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("geckos:block/" + block.getId().getPath()));
    }

    /**
     * Generates a simple block item model using a suffixed block model path.
     *
     * @param block deferred block handle
     * @param a path suffix appended to the block id
     */
    private void blockItem(DeferredBlock<?> block, String a) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("geckos:block/" + block.getId().getPath() + a));
    }
}
