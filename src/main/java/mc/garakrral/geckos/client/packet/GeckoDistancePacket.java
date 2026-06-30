package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeckoDistancePacket(int geckoId, float distance) {

    public static void encode(GeckoDistancePacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.geckoId());
        buf.writeFloat(msg.distance());
    }

    public static GeckoDistancePacket decode(FriendlyByteBuf buf) {
        return new GeckoDistancePacket(buf.readInt(), buf.readFloat());
    }

    public static void handle(GeckoDistancePacket payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Entity entity = player.level().getEntity(payload.geckoId());
            if (!(entity instanceof GeckoEntity gecko)) return;

            gecko.setCarryDistance(Mth.clamp(payload.distance(), 0.8F, 3.0F));
        });
        ctx.get().setPacketHandled(true);
    }
}