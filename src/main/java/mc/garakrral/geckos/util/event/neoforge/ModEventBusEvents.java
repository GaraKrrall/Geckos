/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event.neoforge;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.client.model.FlyModel;
import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.entity.animal.FlyEntity;
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.client.packet.ModPackets;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Central registration hub for mod-lifecycle events dispatched on the NeoForge MOD event bus.
 *
 * <p>This class exists to keep startup registrations in one place instead of spreading them across
 * unrelated bootstrap classes. The methods here are intentionally static because NeoForge discovers
 * and invokes them reflectively through {@link EventBusSubscriber}. That means there is no manual
 * instantiation path and no object state to preserve between invocations.
 *
 * <p>The responsibilities covered here are:
 * <ul>
 *     <li>declaring client model layer definitions so baked model parts can be resolved later,</li>
 *     <li>binding entity attribute suppliers to registered entity types,</li>
 *     <li>declaring spawn placement rules used by vanilla and NeoForge spawn validation, and</li>
 *     <li>registering custom packet payload handlers used by the mod networking layer.</li>
 * </ul>
 *
 * <p>Even though these handlers are short, documenting them matters because lifecycle timing is a
 * common source of mistakes in NeoForge mods. Registering the same thing on the wrong bus, wrong
 * side, or wrong phase usually fails at runtime instead of producing a clean compiler error.
 */
@SuppressWarnings("removal")
@EventBusSubscriber(modid = Geckos.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModEventBusEvents {

    private ModEventBusEvents() {
    }

    /**
     * Registers layer definitions for entity models rendered on the client.
     *
     * <p>NeoForge fires this event during client bootstrap before entity renderers begin asking the
     * model bakery for named layer parts. Each call associates a stable {@code ModelLayerLocation}
     * with a factory method that builds the corresponding {@code LayerDefinition}. Later, renderer
     * constructors resolve those layer locations through the baked model set and expect them to
     * exist exactly once.
     *
     * <p>The gecko and fly models are registered separately because they expose different geometry,
     * texture layout expectations, animation bone names, and renderer scale assumptions. If either
     * layer is omitted, the renderer that depends on it will usually fail during model baking or
     * first render with a missing-part style error.
     *
     * @param event client-side layer definition registration event supplied by NeoForge during MOD
     *              bus initialization
     */
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerLayers(final EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(GeckoModel.GECKO_LAYER_LOCATION, GeckoModel::createBodyLayer);
        event.registerLayerDefinition(FlyModel.LAYER_LOCATION, FlyModel::createBodyLayer);
    }

    /**
     * Attaches fully built attribute maps to each custom entity type owned by the mod.
     *
     * <p>Attribute registration is part of common setup, not rendering, because both logical sides
     * need consistent entity definitions. NeoForge expects every living custom entity to have its
     * attribute supplier bound during this event; otherwise entity creation can fail when the game
     * tries to spawn or deserialize the mob.
     *
     * <p>The method delegates attribute construction to the entity classes themselves so gameplay
     * values remain close to the code that consumes them. That keeps startup wiring thin while still
     * making the registration phase explicit and easy to audit.
     *
     * @param event entity attribute creation event emitted once during mod bootstrap on the MOD bus
     */
    @SubscribeEvent
    public static void registerAttributes(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.GECKO.get(), GeckoEntity.createAttributes().build());
        event.put(ModEntities.FLY.get(), FlyEntity.createAttribute().build());
    }

    /**
     * Declares how the game should validate spawn locations for the mod's entity types.
     *
     * <p>Spawn placement rules combine three pieces of information:
     * <ul>
     *     <li>the broad placement family such as ground-based or unrestricted,</li>
     *     <li>the heightmap sampled to determine a candidate Y coordinate, and</li>
     *     <li>the predicate used to decide whether a concrete spawn attempt is legal.</li>
     * </ul>
     *
     * <p>The gecko intentionally follows standard land-animal rules so it behaves predictably with
     * terrain, while the fly uses a looser placement type because its navigation and movement model
     * are airborne. Using {@link RegisterSpawnPlacementsEvent.Operation#REPLACE} makes the intent
     * explicit: these rules are the authoritative definitions for the mod entity types.
     *
     * @param event spawn placement registration event fired during common mod initialization
     */
    @SubscribeEvent
    public static void registerSpawnPlacements(final RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.GECKO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        event.register(
                ModEntities.FLY.get(),
                SpawnPlacementTypes.NO_RESTRICTIONS,
                Heightmap.Types.WORLD_SURFACE,
                FlyEntity::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    /**
     * Registers custom network payload handlers used by this mod.
     *
     * <p>NeoForge payload registration is versioned. The string passed to the registrar acts as a
     * protocol discriminator so both sides can agree on handler layout and codec expectations. If
     * that version changes, mismatched clients and servers will negotiate accordingly instead of
     * silently decoding incompatible packets.
     *
     * <p>The actual packet list is delegated to {@link ModPackets} to keep this lifecycle class
     * focused on registration timing rather than packet inventory. That separation makes it easier
     * to expand the protocol later without turning this class into a long list of transport details.
     *
     * @param event packet handler registration event emitted on the MOD bus during network setup
     */
    @SubscribeEvent
    public static void registerPackets(final RegisterPayloadHandlersEvent event) {
        ModPackets.register(event.registrar("1"));
    }
}
