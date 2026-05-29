package mc.garakrral.geckos.worldgen;

import java.util.List;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.ModEntities;

import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModBiomeModifiers {

    public static final ResourceKey<BiomeModifier> GECKO_SPAWN = registerKey("gecko");
    public static final ResourceKey<BiomeModifier> FLY_SPAWN = registerKey("fly_spawn");
    @Deprecated(forRemoval = true) public static final ResourceKey<BiomeModifier> RED_TREE_SPAWN = registerKey("red_tree");

    public static void bootstrap(BootstrapContext<BiomeModifier> c) {
        var placedFeatures = c.lookup(Registries.PLACED_FEATURE);
        var biomes = c.lookup(Registries.BIOME);

        c.register(GECKO_SPAWN, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SWAMP), biomes.getOrThrow(Biomes.PLAINS)),
                List.of(new MobSpawnSettings.SpawnerData(ModEntities.GECKO.get(), 20, 2, 4))
        ));
        c.register(FLY_SPAWN, new BiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(
                        biomes.getOrThrow(Biomes.FOREST),
                        biomes.getOrThrow(Biomes.PLAINS),
                        biomes.getOrThrow(Biomes.SWAMP),
                        biomes.getOrThrow(Biomes.DARK_FOREST),
                        biomes.getOrThrow(Biomes.SAVANNA),
                        biomes.getOrThrow(Biomes.BIRCH_FOREST),
                        biomes.getOrThrow(Biomes.JUNGLE),
                        biomes.getOrThrow(Biomes.DESERT)
                ),
                List.of(
                        new MobSpawnSettings.SpawnerData(
                                ModEntities.FLY.get(), 10, 2, 8
                        )
                )
        ));

    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(Main.MODID, name));
    }

}
