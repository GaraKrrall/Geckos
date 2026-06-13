/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.handler;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.client.keybind.KeyBindings;
import mc.garakrral.geckos.client.packet.GeckoCarryPacket;
import mc.garakrral.geckos.client.packet.GeckoDismountPacket;
import mc.garakrral.geckos.client.packet.GeckoMountPacket;
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.client.gui.screen.HudEditScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static mc.garakrral.geckos.util.event.neoforge.GeckoCarryEvents.carriedGecko;

/**
 * Client tick listener that translates key presses into GUI actions and network packets.
 *
 * <p>This class is subscribed directly to the client-side event bus so it can poll key mappings at
 * the end of each client tick. When a relevant key is consumed, it either opens a local screen or
 * emits a packet to request a corresponding server-side gameplay action.
 */
@EventBusSubscriber(modid = Geckos.MODID, value = Dist.CLIENT)
public class KeyInputHandler {
    /**
     * Processes one client tick worth of key input for gecko interactions.
     *
     * @param e post-tick client event used as the polling hook
     */
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();

        if (KeyBindings.HEAD.consumeClick())
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                PacketDistributor.sendToServer(new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.HEAD));
            }

        if (KeyBindings.LEFT.consumeClick())
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                PacketDistributor.sendToServer(new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.LEFT));
            }

        if (KeyBindings.RIGHT.consumeClick())
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                PacketDistributor.sendToServer(new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.RIGHT));
            }

        if (KeyBindings.DISMOUNT_KEY.consumeClick()) PacketDistributor.sendToServer(new GeckoDismountPacket());

        if (KeyBindings.HUD_EDITOR.consumeClick()) {
            Minecraft.getInstance().setScreen(new HudEditScreen());
        }

        if (KeyBindings.CARRY_GECKO.consumeClick()) {
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                if (carriedGecko != null && carriedGecko.getId() == gecko.getId()) {
                    PacketDistributor.sendToServer(new GeckoCarryPacket(gecko.getId(), false));
                    carriedGecko = null;
                } else {
                    PacketDistributor.sendToServer(new GeckoCarryPacket(gecko.getId(), true));
                    carriedGecko = gecko;
                }
            }
        }
    }
}
