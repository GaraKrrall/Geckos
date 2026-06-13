/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.util.event.ClientAddPlayerLayerEvent;
import mc.garakrral.geckos.util.event.ClientKeyRegisterEvents;
import mc.garakrral.geckos.util.event.ClientRendererEvents;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

/**
 * Client-side bootstrap entry point for mod lifecycle events.
 *
 * <p>This class is the client counterpart to the main mod bootstrap and delegates actual work to
 * narrower helper classes. That keeps the subscriber readable while still making all client
 * lifecycle hooks discoverable in one place.
 */
@EventBusSubscriber(modid = Geckos.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GeckosClient {
    /**
     * Handles early client setup tasks such as renderer registration.
     *
     * @param event client setup lifecycle event
     */
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRendererEvents.registerEntityRenderers(event);
    }

    /**
     * Registers all client key mappings used by the mod.
     *
     * @param event key mapping registration event
     */
    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        ClientKeyRegisterEvents.registerKeys(event);
    }

    /**
     * Adds custom player render layers after renderers are available.
     *
     * @param event player layer registration event
     */
    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        ClientAddPlayerLayerEvent.addPlayerLayers(event);
    }
}
