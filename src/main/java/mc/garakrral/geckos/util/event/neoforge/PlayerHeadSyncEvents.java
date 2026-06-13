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
import mc.garakrral.geckos.client.packet.SyncHeadGeckoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Synchronizes the custom "gecko on head" attachment state to interested clients.
 *
 * <p>The attachment itself lives on the player entity, but attachments are only useful for visual
 * features when remote clients know about their current content. This event subscriber updates
 * that client-side mirror at important lifecycle boundaries: initial login, respawn, and when a
 * viewer starts tracking another player.
 *
 * <p>Each handler exits early unless the relevant participants are {@link ServerPlayer} instances,
 * because packet emission is only meaningful on the logical server.
 */
@EventBusSubscriber(modid = Geckos.MODID)
public final class PlayerHeadSyncEvents {

    private PlayerHeadSyncEvents() {
    }

    /**
     * Pushes the player's current head-gecko state immediately after login.
     *
     * <p>This avoids a stale first-render window where the joining player or nearby clients would
     * otherwise see no head-mounted gecko until some later gameplay action triggered a sync.
     *
     * @param event login event carrying the player that just entered the server
     */
    @SubscribeEvent
    public static void onLogin(final PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
    }

    /**
     * Re-sends attachment state after respawn.
     *
     * <p>Respawn transitions often recreate or reinitialize player-side state, so explicitly
     * sending the current payload prevents visual desynchronization after death or dimension-style
     * respawn flows.
     *
     * @param event respawn event emitted once the replacement server player is available
     */
    @SubscribeEvent
    public static void onRespawn(final PlayerEvent.PlayerRespawnEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
    }

    /**
     * Sends head-gecko data to a viewer when they begin tracking another player.
     *
     * <p>This is the normal path for late joiners and players entering render distance after the
     * owner was already online. Without this targeted sync, the viewer would not know the tracked
     * player's attachment contents until some later packet happened to refresh it.
     *
     * @param event tracking event containing both the viewer and the entity now being tracked
     */
    @SubscribeEvent
    public static void onStartTracking(final PlayerEvent.StartTracking event) {
        if (!(event.getEntity() instanceof ServerPlayer viewer)) return;
        if (!(event.getTarget() instanceof ServerPlayer owner)) return;
        SyncHeadGeckoPacket.sendToViewer(viewer, owner);
    }
}
