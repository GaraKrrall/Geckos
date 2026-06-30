package mc.garakrral.geckos.util.event.forge;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.client.model.FlyModel;
import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.client.packet.ModPackets;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.entity.animal.FlyEntity;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public final class ModEventBusEvents {
    private ModEventBusEvents() {
    }

    @Mod.EventBusSubscriber(modid = Geckos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static final class ClientEvents {
        private ClientEvents() {
        }

        @SubscribeEvent
        public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
            event.registerLayerDefinition(GeckoModel.LAYER_LOCATION, GeckoModel::createBodyLayer);
            event.registerLayerDefinition(FlyModel.LAYER_LOCATION, FlyModel::createBodyLayer);
        }
    }

    @Mod.EventBusSubscriber(modid = Geckos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class CommonEvents {
        private CommonEvents() {
        }

        @SubscribeEvent
        public static void registerAttributes(EntityAttributeCreationEvent event) {
            event.put(ModEntities.GECKO.get(), GeckoEntity.createAttributes().build());
            event.put(ModEntities.FLY.get(), FlyEntity.createAttribute().build());
        }

        public static void registerSpawnPlacements() {
            SpawnPlacements.register(
                    ModEntities.GECKO.get(),
                    SpawnPlacements.Type.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Animal::checkAnimalSpawnRules
            );

            SpawnPlacements.register(
                    ModEntities.FLY.get(),
                    SpawnPlacements.Type.NO_RESTRICTIONS,
                    Heightmap.Types.WORLD_SURFACE,
                    FlyEntity::checkMobSpawnRules
            );
        }

        public static void registerPackets() {
            ModPackets.register();
        }
    }
}