/*
 *

 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *

 */

package mc.garakrral.geckos.util.event;

import mc.garakrral.geckos.client.model.layer.GeckoOnShoulderLayer;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**

 * Adds custom render layers to vanilla player renderers.
 */
@OnlyIn(Dist.CLIENT)
public class ClientAddPlayerLayerEvent {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAddPlayerLayerEvent.class);

    /**

     * Attaches gecko render layers to every known player skin renderer.
     */
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (String skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);

            if (renderer != null) {
                renderer.addLayer(new GeckoOnShoulderLayer<>(
                        renderer,
                        event.getEntityModels()
                ));

                LOGGER.info("Added Gecko Layer {}", skin);
                LOGGER.warn("The pick up geckos feature is not supported in Forge 1.20.1. Skin: {}", skin);
            }
        }
    }
}
