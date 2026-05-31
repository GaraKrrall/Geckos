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

@EventBusSubscriber(modid = Geckos.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class GeckosClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ClientRendererEvents.registerEntityRenderers(event);
    }

    @SubscribeEvent
    public static void onKeyRegister(RegisterKeyMappingsEvent event) {
        ClientKeyRegisterEvents.registerKeys(event);
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        ClientAddPlayerLayerEvent.addPlayerLayers(event);
    }
}
