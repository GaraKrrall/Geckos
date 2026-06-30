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
import mc.garakrral.geckos.client.gui.screen.HudEditScreen;
import mc.garakrral.geckos.client.keybind.KeyBindings;
import mc.garakrral.geckos.client.packet.GeckoCarryPacket;
import mc.garakrral.geckos.client.packet.GeckoDismountPacket;
import mc.garakrral.geckos.client.packet.GeckoMountPacket;
import mc.garakrral.geckos.client.packet.ModPackets;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static mc.garakrral.geckos.util.event.forge.GeckoCarryEvents.carriedGecko;

/**
 * Client tick listener that translates key presses into GUI actions and network packets.
 */
@Mod.EventBusSubscriber(modid = Geckos.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class KeyInputHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.level == null) {
            return;
        }

        if (KeyBindings.HEAD.consumeClick()) {
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                ModPackets.INSTANCE.sendToServer(
                        new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.HEAD)
                );
            }
        }

        if (KeyBindings.LEFT.consumeClick()) {
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                ModPackets.INSTANCE.sendToServer(
                        new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.LEFT)
                );
            }
        }

        if (KeyBindings.RIGHT.consumeClick()) {
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                ModPackets.INSTANCE.sendToServer(
                        new GeckoMountPacket(gecko.getId(), GeckoMountPacket.MountType.RIGHT)
                );
            }
        }

        if (KeyBindings.DISMOUNT_KEY.consumeClick()) {
            ModPackets.INSTANCE.sendToServer(new GeckoDismountPacket());
        }

        if (KeyBindings.HUD_EDITOR.consumeClick()) {
            mc.setScreen(new HudEditScreen());
        }

        if (KeyBindings.CARRY_GECKO.consumeClick()) {
            if (mc.hitResult instanceof EntityHitResult hit && hit.getEntity() instanceof GeckoEntity gecko) {
                if (carriedGecko != null && carriedGecko.getId() == gecko.getId()) {
                    ModPackets.INSTANCE.sendToServer(new GeckoCarryPacket(gecko.getId(), false));
                    carriedGecko = null;
                } else {
                    ModPackets.INSTANCE.sendToServer(new GeckoCarryPacket(gecko.getId(), true));
                    carriedGecko = gecko;
                }
            }
        }
    }
}