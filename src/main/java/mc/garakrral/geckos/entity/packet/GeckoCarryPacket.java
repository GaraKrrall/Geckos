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

public record GeckoCarryPacket(int geckoId, boolean carry)
        implements CustomPacketPayload {

    public static final Type<GeckoCarryPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    Main.MODID,
                    "gecko_carry"
            ));

    public static final StreamCodec<
            RegistryFriendlyByteBuf,
            GeckoCarryPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    GeckoCarryPacket::geckoId,

                    ByteBufCodecs.BOOL,
                    GeckoCarryPacket::carry,

                    GeckoCarryPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(
            GeckoCarryPacket payload,
            IPayloadContext ctx
    ) {
        ctx.enqueueWork(() -> {

            ServerPlayer player =
                    (ServerPlayer) ctx.player();

            Entity e =
                    player.level()
                            .getEntity(payload.geckoId());

            if (!(e instanceof GeckoEntity gecko))
                return;

            gecko.setCarried(payload.carry());
        });
    }
}