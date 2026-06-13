/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event;

import mc.garakrral.geckos.client.model.layer.GeckoOnHeadLayer;
import mc.garakrral.geckos.client.model.layer.GeckoOnShoulderLayer;

import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Adds custom render layers to vanilla player renderers.
 *
 * <p>The mod uses these layers to draw geckos on players' shoulders and heads without replacing
 * the base player renderer. The layer attachment step runs after vanilla skins and player renderers
 * are known to the client render system.
 */
@OnlyIn(Dist.CLIENT)
public class ClientAddPlayerLayerEvent {
    private static Logger LOGGER = LoggerFactory.getLogger(ClientAddPlayerLayerEvent.class);

    /**
     * Attaches gecko render layers to every known player skin renderer.
     *
     * @param event layer registration event providing access to player renderers and baked models
     */
    public static void addPlayerLayers(EntityRenderersEvent.AddLayers event) {
        for (PlayerSkin.Model skin : event.getSkins()) {
            PlayerRenderer renderer = event.getSkin(skin);

            if (renderer != null) {
                renderer.addLayer(new GeckoOnShoulderLayer<>(
                        renderer,
                        event.getEntityModels()
                ));

                renderer.addLayer(
                        new GeckoOnHeadLayer<>(
                                renderer,
                                event.getEntityModels()
                        ));

                LOGGER.info("Added Gecko Layer {}", skin);
            }
        }
    }
}
