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

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**

 * Client-side bootstrap entry point for mod lifecycle events.
 */
@Mod.EventBusSubscriber(modid = Geckos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
