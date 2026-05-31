/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.entity.packet;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.attachment.ModAttachments;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncHeadGeckoPacket(int playerId, CompoundTag tag) implements CustomPacketPayload {

    public static final Type<SyncHeadGeckoPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "sync_head_gecko"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncHeadGeckoPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    SyncHeadGeckoPacket::playerId,
                    ByteBufCodecs.COMPOUND_TAG,
                    SyncHeadGeckoPacket::tag,
                    SyncHeadGeckoPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(SyncHeadGeckoPacket payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            if (mc.level == null) return;

            Entity e = mc.level.getEntity(payload.playerId());
            if (e instanceof Player player) {
                player.setData(ModAttachments.HEAD_GECKO.get(), payload.tag());
            }
        });
    }

    public static void sendToTrackersAndSelf(ServerPlayer owner) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(
                owner,
                new SyncHeadGeckoPacket(owner.getId(), owner.getData(ModAttachments.HEAD_GECKO.get()))
        );
    }

    public static void sendToViewer(ServerPlayer viewer, ServerPlayer owner) {
        PacketDistributor.sendToPlayer(
                viewer,
                new SyncHeadGeckoPacket(owner.getId(), owner.getData(ModAttachments.HEAD_GECKO.get()))
        );
    }
}