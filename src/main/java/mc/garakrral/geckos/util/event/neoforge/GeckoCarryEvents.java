/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.event.neoforge;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.custom.GeckoEntity;
import mc.garakrral.geckos.entity.packet.GeckoDistancePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Geckos.MODID, value = Dist.CLIENT)
public class GeckoCarryEvents {
    public static GeckoEntity carriedGecko;

    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollingEvent e) {
        if (carriedGecko == null) return;
        if (!carriedGecko.isCarried()) return;

        e.setCanceled(true);

        float dist = carriedGecko.getCarryDistance();

        dist += (float) e.getScrollDeltaY() * 0.2F;
        dist = Mth.clamp(dist, GeckoEntity.MIN_CARRY_DISTANCE, GeckoEntity.MAX_CARRY_DISTANCE);

        carriedGecko.setCarryDistance(dist);

        PacketDistributor.sendToServer(
                new GeckoDistancePacket(
                        carriedGecko.getId(),
                        dist
                )
        );
    }
}
