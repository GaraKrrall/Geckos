/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.packet;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers every custom network payload used by the mod.
 *
 * <p>The payload registrar already encapsulates version negotiation and direction information. This
 * helper simply groups the packet inventory so setup code remains concise and future additions stay
 * in one place.
 */
public class ModPackets {
    /**
     * Registers packet codecs and handlers with the provided registrar.
     *
     * @param registrar versioned payload registrar for the current network channel
     */
    public static void register(PayloadRegistrar registrar) {
        registrar.playToServer(
                GeckoDismountPacket.TYPE,
                GeckoDismountPacket.CODEC,
                GeckoDismountPacket::handle
        );
        registrar.playToServer(
                GeckoMountPacket.TYPE,
                GeckoMountPacket.CODEC,
                GeckoMountPacket::handle
        );
        registrar.playToClient(
                SyncHeadGeckoPacket.TYPE,
                SyncHeadGeckoPacket.CODEC,
                SyncHeadGeckoPacket::handle
        );
        registrar.playToServer(
                GeckoCarryPacket.TYPE,
                GeckoCarryPacket.CODEC,
                GeckoCarryPacket::handle
        );
        registrar.playToServer(
                GeckoDistancePacket.TYPE,
                GeckoDistancePacket.CODEC,
                GeckoDistancePacket::handle
        );
    }
}
