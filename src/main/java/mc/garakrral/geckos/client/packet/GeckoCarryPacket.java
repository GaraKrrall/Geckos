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
 * Client-to-server payload that toggles whether a gecko is being actively carried.
 *
 * @param geckoId runtime entity id of the targeted gecko
 * @param carry desired carry state to apply on the server
 */
public record GeckoCarryPacket(int geckoId, boolean carry)
        implements CustomPacketPayload {

    public static final Type<GeckoCarryPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    Geckos.MODID,
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

    /**
     * Returns the payload type identifier used by NeoForge networking.
     *
     * @return packet type token for this payload
     */
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /**
     * Handles the packet on the server by updating the target gecko's carry flag.
     *
     * @param payload decoded packet payload
     * @param ctx payload handling context supplied by NeoForge
     */
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
