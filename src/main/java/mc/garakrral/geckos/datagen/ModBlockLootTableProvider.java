package mc.garakrral.geckos.datagen;

import java.util.Set;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import mc.garakrral.geckos.block.ModBlocks;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {

        this.dropSelf(ModBlocks.RED_WOOD_LOG.get());
        this.dropSelf(ModBlocks.RED_WOOD_PLANKS.get());
        this.dropSelf(ModBlocks.STRIPPED_RED_WOOD_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_RED_WOOD.get());
        this.dropSelf(ModBlocks.RED_WOOD.get());
        this.dropSelf(ModBlocks.RED_WOOD_SAPLING.get());

        this.add(ModBlocks.RED_WOOD_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.RED_WOOD_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}