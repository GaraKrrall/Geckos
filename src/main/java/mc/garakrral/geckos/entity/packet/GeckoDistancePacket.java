package mc.garakrral.geckos.entity.packet;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GeckoDistancePacket(int geckoId, float distance) implements CustomPacketPayload {
    public static final Type<GeckoDistancePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "gecko_distance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GeckoDistancePacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, GeckoDistancePacket::geckoId,
                    ByteBufCodecs.FLOAT, GeckoDistancePacket::distance,
                    GeckoDistancePacket::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GeckoDistancePacket payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) return;

            Entity entity = player.level().getEntity(payload.geckoId());

            if (!(entity instanceof GeckoEntity gecko)) return;
            gecko.setCarryDistance(Math.max(0.8F, Math.min(payload.distance(), 3F)));
        });
    }
}