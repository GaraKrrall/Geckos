package mc.garakrral.geckos.entity.packet;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {
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