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

@EventBusSubscriber(modid = Geckos.MODID)
public class NeoForgeEventBusEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        CompoundTag left = player.getShoulderEntityLeft();
        CompoundTag right = player.getShoulderEntityRight();

        if (isGecko(left) || isGecko(right)) {

            try {
                var field = Player.class.getDeclaredField("timeEntitySatOnShoulder");
                field.setAccessible(true);
                field.setLong(player, player.level().getGameTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isGecko(CompoundTag tag) {
        return !tag.isEmpty() && tag.getString("id").equals("geckos:gecko");
    }
}
