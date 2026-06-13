/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.block;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.item.ModItems;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Central block registry declarations for the mod.
 *
 * <p>This class owns the deferred block register and helper methods used to register both block
 * instances and their matching item forms. Keeping the registration pattern here reduces duplicated
 * bootstrap code in feature-specific classes.
 */
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Geckos.MODID);

    public static final DeferredBlock<Block> GECKO_STATUE = registerBlock("gecko_statue",
            () -> new GeckoStatueBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));


    /**
     * Registers a block and automatically creates its corresponding inventory item.
     *
     * @param name registry name for both the block and its block item
     * @param block supplier that constructs the block instance
     * @param <T> concrete block type being registered
     * @return deferred handle pointing at the registered block
     */
    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    /**
     * Registers the item representation for a previously declared block.
     *
     * @param name registry name shared with the block
     * @param block deferred handle of the block whose item should be exposed
     * @param <T> concrete block type wrapped by the deferred handle
     */
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
