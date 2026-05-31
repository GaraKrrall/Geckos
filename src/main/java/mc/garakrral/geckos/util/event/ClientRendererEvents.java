/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event;

import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.client.renderer.FlyRenderer;
import mc.garakrral.geckos.client.renderer.GeckoRenderer;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class ClientRendererEvents {
    public static void registerEntityRenderers(FMLClientSetupEvent event) {
        EntityRenderers.register(ModEntities.GECKO.get(), GeckoRenderer::new);
        EntityRenderers.register(ModEntities.FLY.get(), FlyRenderer::new);
    }
}
