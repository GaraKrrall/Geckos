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
import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GeckoMountPacket(int entityId, MountType mountType) implements CustomPacketPayload {

    public enum MountType {
        HEAD,
        LEFT,
        RIGHT
    }

    public static final CustomPacketPayload.Type<GeckoMountPacket> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "gecko_mount")
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, GeckoMountPacket> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,
                    GeckoMountPacket::entityId,
                    ByteBufCodecs.STRING_UTF8,
                    p -> p.mountType.name(),
                    (id, type) -> new GeckoMountPacket(id, MountType.valueOf(type))
            );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GeckoMountPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;

            Entity e = player.level().getEntity(payload.entityId());
            if (!(e instanceof GeckoEntity gecko)) return;
            if (!gecko.isTame() || gecko.getOwner() != player) return;
            if (gecko.isSleepingGecko()) return;
            if (gecko.isBaby()) return;

            CompoundTag save = new CompoundTag();
            gecko.saveWithoutId(save);
            save.putString("id", "geckos:gecko");

            switch (payload.mountType) {
                case HEAD -> {
                    if (!player.getData(ModAttachments.HEAD_GECKO.get()).isEmpty()) return;

                    save.putBoolean("OnHead", true);
                    player.setData(ModAttachments.HEAD_GECKO.get(), save);

                    SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
                }

                case LEFT -> {
                    if (!player.getShoulderEntityLeft().isEmpty()) return;
                    setShoulder(player, save, false);
                }

                case RIGHT -> {
                    if (!player.getShoulderEntityRight().isEmpty()) return;
                    setShoulder(player, save, true);
                }
            }

            gecko.discard();
        });
    }

    private static void setShoulder(ServerPlayer player, CompoundTag tag, boolean right) {
        try {
            var method = Player.class.getDeclaredMethod(
                    right ? "setShoulderEntityRight" : "setShoulderEntityLeft",
                    CompoundTag.class
            );
            method.setAccessible(true);
            method.invoke(player, tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}