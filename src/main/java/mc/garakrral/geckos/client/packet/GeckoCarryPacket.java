package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeckoCarryPacket(int geckoId, boolean carry) {

    public static void encode(GeckoCarryPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.geckoId());
        buf.writeBoolean(msg.carry());
    }

    public static GeckoCarryPacket decode(FriendlyByteBuf buf) {
        return new GeckoCarryPacket(buf.readInt(), buf.readBoolean());
    }

    public static void handle(GeckoCarryPacket payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Entity e = player.level().getEntity(payload.geckoId());
            if (!(e instanceof GeckoEntity gecko)) return;

            gecko.setCarried(payload.carry());
        });
        ctx.get().setPacketHandled(true);
    }
}