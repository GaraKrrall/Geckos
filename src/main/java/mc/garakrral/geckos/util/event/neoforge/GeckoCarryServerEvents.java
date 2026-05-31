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

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = Geckos.MODID)
public class GeckoCarryServerEvents {

    private static boolean isCarryingGecko(Player player) {
        return !player.level().getEntitiesOfClass(
                GeckoEntity.class,
                player.getBoundingBox().inflate(64.0D),
                g -> g.isCarried() && player.equals(g.getOwner())
        ).isEmpty();
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem e) {
        if (e.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(e.getEntity())) return;

        e.setCanceled(true);
        e.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock e) {
        if (e.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(e.getEntity())) return;

        e.setCanceled(true);
        e.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract e) {
        if (e.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(e.getEntity())) return;

        e.setCanceled(true);
        e.setCancellationResult(InteractionResult.FAIL);
    }

    @SubscribeEvent
    public static void onAttack(AttackEntityEvent e) {
        if (e.getEntity().level().isClientSide()) return;
        if (!isCarryingGecko(e.getEntity())) return;

        e.setCanceled(true);
    }
}
