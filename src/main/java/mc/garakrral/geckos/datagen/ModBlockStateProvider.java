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

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Geckos.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
       logBlock(((RotatedPillarBlock) ModBlocks.RED_WOOD_LOG.get()));
       logBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_RED_WOOD_LOG.get()));
       axisBlock(((RotatedPillarBlock) ModBlocks.RED_WOOD.get()), blockTexture(ModBlocks.RED_WOOD_LOG.get()), blockTexture(ModBlocks.RED_WOOD_LOG.get()));
       axisBlock(((RotatedPillarBlock) ModBlocks.STRIPPED_RED_WOOD.get()), blockTexture(ModBlocks.STRIPPED_RED_WOOD_LOG.get()), blockTexture(ModBlocks.STRIPPED_RED_WOOD_LOG.get()));
       blockItem(ModBlocks.RED_WOOD_LOG);
       blockItem(ModBlocks.RED_WOOD);
       blockItem(ModBlocks.STRIPPED_RED_WOOD_LOG);
       blockItem(ModBlocks.STRIPPED_RED_WOOD);
       blockWithItem(ModBlocks.RED_WOOD_PLANKS);
       leavesBlock(ModBlocks.RED_WOOD_LEAVES);
    }

    private void saplingBlock(DeferredBlock<Block> object) {
        simpleBlock(object.get(),
                models().cross(BuiltInRegistries.BLOCK.getKey(object.get()).getPath(), blockTexture(object.get())).renderType("cutout"));
    }

    private void leavesBlock(DeferredBlock<Block> object) {
        simpleBlockWithItem(object.get(),
                models().singleTexture(BuiltInRegistries.BLOCK.getKey(object.get()).getPath(), ResourceLocation.parse("minecraft:block/leaves"),
                        "all", blockTexture(object.get())).renderType("cutout"));
    }

    private void blockWithItem(DeferredBlock<?> block) {
        simpleBlockWithItem(block.get(), cubeAll(block.get()));
    }

    private void blockItem(DeferredBlock<?> block) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("geckos:block/" + block.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> block, String a) {
        simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile("geckos:block/" + block.getId().getPath() + a));
    }
}