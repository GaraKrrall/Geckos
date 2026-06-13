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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

/**
 * Contains general-purpose NeoForge game-bus hooks that do not fit a more specific subsystem.
 *
 * <p>At the moment this class keeps shoulder-riding geckos from being dropped by vanilla's normal
 * inactivity timer logic. The implementation is intentionally isolated because it relies on a
 * reflective field write, which is exactly the kind of compatibility-sensitive behavior that
 * benefits from being documented close to the code.
 */
@EventBusSubscriber(modid = Geckos.MODID)
public final class NeoForgeEventBusEvents {

    private NeoForgeEventBusEvents() {
    }

    /**
     * Refreshes the internal shoulder occupancy timer for players currently carrying geckos on one
     * of their shoulders.
     *
     * <p>Vanilla tracks when a shoulder entity last "sat" on the player and may later use that
     * timestamp as part of its own release logic. Because this mod stores geckos in shoulder NBT
     * for longer-lived cosmetic/gameplay behavior, the timer needs to be continuously refreshed so
     * vanilla does not treat the entity as ready to fall off unexpectedly.
     *
     * <p>The method runs only on the logical server. Client execution would be useless and could
     * create misleading state assumptions because the authoritative shoulder data lives server-side.
     *
     * @param event post-tick event for a player; fired once per player per server tick
     */
    @SubscribeEvent
    public static void onPlayerTick(final PlayerTickEvent.Post event) {
        final Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        final CompoundTag left = player.getShoulderEntityLeft();
        final CompoundTag right = player.getShoulderEntityRight();

        if (isGecko(left) || isGecko(right)) {
            try {
                final var field = Player.class.getDeclaredField("timeEntitySatOnShoulder");
                field.setAccessible(true);
                field.setLong(player, player.level().getGameTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Checks whether a shoulder entity tag belongs to this mod's gecko entity.
     *
     * <p>The test is intentionally lightweight because it runs every tick for players that may have
     * shoulder occupants. It does not deserialize the entity; it only validates the serialized
     * entity identifier stored in the tag.
     *
     * @param tag shoulder NBT payload taken from the player
     * @return {@code true} when the payload is non-empty and represents {@code geckos:gecko}
     */
    private static boolean isGecko(final CompoundTag tag) {
        return !tag.isEmpty() && tag.getString("id").equals("geckos:gecko");
    }
}
