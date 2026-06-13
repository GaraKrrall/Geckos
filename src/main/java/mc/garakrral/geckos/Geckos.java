/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos;

import mc.garakrral.geckos.attachment.ModAttachments;
import mc.garakrral.geckos.block.ModBlocks;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.item.group.ModItemGroups;
import mc.garakrral.geckos.util.function.ClientStartFunctions;
import mc.garakrral.geckos.util.function.ServerStartFunctions;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

/**
 * Primary mod entry point discovered by NeoForge through the {@link Mod} annotation.
 *
 * <p>This class wires together the registry bootstrap, common lifecycle listeners, and global
 * event-bus subscriptions needed for the mod to function. It intentionally keeps the body light so
 * startup responsibilities stay centralized without embedding unrelated gameplay logic directly in
 * the mod constructor.
 */
@Mod(Geckos.MODID)
public class Geckos {
    public static final String MODID = "geckos";
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Creates and initializes the mod bootstrap object.
     *
     * <p>NeoForge invokes this constructor during mod loading after it has prepared the mod event
     * bus and container metadata. The constructor is responsible for registering deferred registers,
     * common event listeners, configuration hooks, and client-only extension points when the current
     * runtime side supports them.
     *
     * @param modEventBus mod-specific event bus used for registration and lifecycle callbacks
     * @param modContainer container representing this loaded mod instance and its metadata
     */
    public Geckos(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(Geckos::commonSetup);

        ModItemGroups.TABS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(Geckos.class);

        ServerStartFunctions.registerConfig(modContainer);
        ModAttachments.register(modEventBus);
        if (FMLLoader.getDist() == Dist.CLIENT) ClientStartFunctions.registerConfigScreen(modContainer);
    }

    /**
     * Handles common setup tasks that should run during the shared lifecycle phase.
     *
     * <p>The current implementation only emits a startup log line, but this callback is the correct
     * place for future cross-side initialization work that depends on deferred registration having
     * completed.
     *
     * @param event common setup lifecycle event fired on the mod event bus
     */
    private static void commonSetup(final FMLCommonSetupEvent event) {
        ServerStartFunctions.printInfoMessage();
    }

    /**
     * Responds to the server-starting event on the global NeoForge event bus.
     *
     * <p>This callback runs when a logical server instance begins starting. It is separate from the
     * common setup phase and is useful for runtime-only initialization or diagnostics that should
     * happen each time a world-backed server session is created.
     *
     * @param event server lifecycle event fired as the integrated or dedicated server starts
     */
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerStartFunctions.printInfoMessage();
    }
}
