/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Client-to-server payload that requests a new carry distance for a gecko.
 *
 * @param geckoId runtime entity id of the gecko being adjusted
 * @param distance requested carry distance
 */
public record GeckoDistancePacket(int geckoId, float distance) implements CustomPacketPayload {
    public static final Type<GeckoDistancePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "gecko_distance"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GeckoDistancePacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT, GeckoDistancePacket::geckoId,
                    ByteBufCodecs.FLOAT, GeckoDistancePacket::distance,
                    GeckoDistancePacket::new);

    /**
     * Returns the payload type identifier used by the networking system.
     *
     * @return packet type token for this payload
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Applies the requested carry distance on the authoritative server entity.
     *
     * @param payload decoded packet payload
     * @param ctx payload handling context
     */
    public static void handle(GeckoDistancePacket payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) return;

            Entity entity = player.level().getEntity(payload.geckoId());

            if (!(entity instanceof GeckoEntity gecko)) return;
            gecko.setCarryDistance(Math.max(0.8F, Math.min(payload.distance(), 3F)));
        });
    }
}
