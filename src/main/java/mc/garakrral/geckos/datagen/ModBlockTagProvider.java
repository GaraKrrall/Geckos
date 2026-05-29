package mc.garakrral.geckos.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.block.ModBlocks;

import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Main.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(BlockTags.LOGS)
                .add(ModBlocks.RED_WOOD_LOG.get())
                .add(ModBlocks.RED_WOOD.get())
                .add(ModBlocks.STRIPPED_RED_WOOD_LOG.get())
                .add(ModBlocks.STRIPPED_RED_WOOD.get());

        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.RED_WOOD_LOG.get())
                .add(ModBlocks.RED_WOOD.get())
                .add(ModBlocks.STRIPPED_RED_WOOD_LOG.get())
                .add(ModBlocks.STRIPPED_RED_WOOD.get());
    }
}