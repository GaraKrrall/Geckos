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
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.client.packet.GeckoDistancePacket;

import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * Client-side input helpers for the gecko carry mechanic.
 *
 * <p>This subscriber translates local mouse-wheel input into a continuously adjustable carry
 * distance. The client performs immediate local feedback for responsiveness and also emits a packet
 * so the server can update the authoritative carry distance used for collision and movement logic.
 */
@EventBusSubscriber(modid = Geckos.MODID, value = Dist.CLIENT)
public final class GeckoCarryEvents {

    /**
     * The gecko currently being manipulated by the local client, if any.
     *
     * <p>This is intentionally kept as transient client state rather than persisted game data. Its
     * purpose is only to know which entity should respond to scroll-wheel distance changes between
     * key presses and packet round trips.
     */
    public static GeckoEntity carriedGecko;

    private GeckoCarryEvents() {
    }

    /**
     * Converts mouse-wheel scrolling into a bounded carry-distance adjustment.
     *
     * <p>If no gecko is currently selected, or if the selected gecko is no longer marked as
     * carried, the event is ignored. Otherwise the scroll input is consumed to prevent other client
     * systems from reacting to the same wheel movement while carry mode is active.
     *
     * @param event client mouse scrolling event containing the raw wheel delta for the current frame
     */
    @SubscribeEvent
    public static void mouseScroll(final InputEvent.MouseScrollingEvent event) {
        if (carriedGecko == null) return;
        if (!carriedGecko.isCarried()) return;

        event.setCanceled(true);

        float dist = carriedGecko.getCarryDistance();

        dist += (float) event.getScrollDeltaY() * 0.2F;
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
