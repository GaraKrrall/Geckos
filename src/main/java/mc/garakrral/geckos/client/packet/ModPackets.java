package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.Geckos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;


public final class ModPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Geckos.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int id = 0;

    private ModPackets() {
    }

    public static void register() {
        INSTANCE.registerMessage(id++, GeckoDismountPacket.class,
                GeckoDismountPacket::encode,
                GeckoDismountPacket::decode,
                GeckoDismountPacket::handle);

        INSTANCE.registerMessage(id++, GeckoMountPacket.class,
                GeckoMountPacket::encode,
                GeckoMountPacket::decode,
                GeckoMountPacket::handle);


        INSTANCE.registerMessage(id++, GeckoCarryPacket.class,
                GeckoCarryPacket::encode,
                GeckoCarryPacket::decode,
                GeckoCarryPacket::handle);

        INSTANCE.registerMessage(id++, GeckoDistancePacket.class,
                GeckoDistancePacket::encode,
                GeckoDistancePacket::decode,
                GeckoDistancePacket::handle);
    }
}