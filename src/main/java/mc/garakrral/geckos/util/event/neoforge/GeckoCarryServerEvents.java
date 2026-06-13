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

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

/**
 * Restricts normal interaction flows while a player is actively carrying a gecko in front of them.
 *
 * <p>The carry mechanic repurposes player input for positional manipulation. While that mode is
 * active, regular right-click and attack actions would compete with the carry interaction and lead
 * to inconsistent UX or accidental world interactions. These handlers therefore short-circuit a
 * small set of common actions whenever the server detects that the player owns a gecko currently
 * marked as carried.
 *
 * <p>This logic intentionally lives on the server because interaction cancellation must be enforced
 * authoritatively. Client-side suppression alone would be easy to bypass and would not protect the
 * game state in multiplayer.
 */
@EventBusSubscriber(modid = Geckos.MODID)
public final class GeckoCarryServerEvents {

    private GeckoCarryServerEvents() {
    }

    /**
     * Determines whether the supplied player currently owns at least one nearby carried gecko.
     *
     * <p>The search is intentionally bounded to a moderate radius instead of scanning the entire
     * world. Carried geckos are expected to remain very close to their owner, so a local query keeps
     * the check inexpensive while still covering valid carry states.
     *
     * @param player player whose carry state should be inspected
     * @return {@code true} if a carried gecko owned by the player is found in range
     */
    private static boolean isCarryingGecko(final Player player) {
        return !player.level().getEntitiesOfClass(
                GeckoEntity.class,
                player.getBoundingBox().inflate(64.0D),
                g -> g.isCarried() && player.equals(g.getOwner())
        ).isEmpty();
    }

    /**
     * Cancels item use while the player is in carry mode.
     *
     * @param event right-click item interaction event fired on both sides
     */
    @SubscribeEvent
    public static void onRightClickItem(final PlayerInteractEvent.RightClickItem event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(event.getEntity())) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }

    /**
     * Cancels block interactions while the player is carrying a gecko.
     *
     * @param event right-click block interaction event
     */
    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(event.getEntity())) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }

    /**
     * Cancels generic entity interaction while carry mode is active.
     *
     * @param event entity interaction event triggered by right-clicking another entity
     */
    @SubscribeEvent
    public static void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(event.getEntity())) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }

    /**
     * Cancels attacks while the player is manipulating a carried gecko.
     *
     * @param event attack event fired when a player attempts to damage an entity
     */
    @SubscribeEvent
    public static void onAttack(final AttackEntityEvent event) {
        if (event.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(event.getEntity())) return;

        event.setCanceled(true);
    }
}
