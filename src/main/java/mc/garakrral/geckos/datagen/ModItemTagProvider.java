package mc.garakrral.geckos.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.block.ModBlocks;

import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(ItemTags.LOGS_THAT_BURN)
                .add(ModBlocks.RED_WOOD_LOG.get().asItem())
                .add(ModBlocks.RED_WOOD.get().asItem())
                .add(ModBlocks.STRIPPED_RED_WOOD_LOG.get().asItem())
                .add(ModBlocks.STRIPPED_RED_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS)
                .add(ModBlocks.RED_WOOD.get().asItem());
    }
}