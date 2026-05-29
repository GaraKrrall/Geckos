package mc.garakrral.geckos.block;

import java.util.function.Supplier;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.block.feature.SimpleLeavesBlock;
import mc.garakrral.geckos.block.feature.SimplePlanksBlock;
import mc.garakrral.geckos.block.feature.SimpleRotatedPillarBlock;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.worldgen.tree.ModTreeGrowers;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Main.MODID);

    public static final DeferredBlock<Block> RED_WOOD_LOG = registerBlock("red_wood_log",
            () -> new SimpleRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));

    public static final DeferredBlock<Block> RED_WOOD = registerBlock("red_wood",
            () -> new SimpleRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));

    public static final DeferredBlock<Block> STRIPPED_RED_WOOD_LOG = registerBlock("stripped_red_wood_log",
            () -> new SimpleRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));

    public static final DeferredBlock<Block> STRIPPED_RED_WOOD = registerBlock("stripped_red_wood",
            () -> new SimpleRotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

    public static final DeferredBlock<Block> RED_WOOD_PLANKS = registerBlock("red_wood_planks",
            () -> new SimplePlanksBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS))
        );

    public static final DeferredBlock<Block> RED_WOOD_LEAVES = registerBlock("red_wood_leaves",
            () -> new SimpleLeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

    public static final DeferredBlock<Block> RED_WOOD_SAPLING = registerBlock("red_wood_sapling",
            () -> new SaplingBlock(ModTreeGrowers.RED_TREE_GROWER ,BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
      DeferredBlock<T> toReturn = BLOCKS.register(name, block);
      registerBlockItem(name, toReturn);
      return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name,  () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
