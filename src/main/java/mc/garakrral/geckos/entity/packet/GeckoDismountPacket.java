package mc.garakrral.geckos.entity.packet;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.attachment.ModAttachments;
import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record GeckoDismountPacket() implements CustomPacketPayload {

    public static final Type<GeckoDismountPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(Main.MODID, "gecko_dismount"));

    public static final StreamCodec<RegistryFriendlyByteBuf, GeckoDismountPacket> CODEC =
            StreamCodec.unit(new GeckoDismountPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(GeckoDismountPacket payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            if (player == null) return;

            if (dropHead(player)) return;

            if (!dropShoulder(player, true))
                dropShoulder(player, false);
        });
    }

    private static boolean dropShoulder(ServerPlayer player, boolean right) {
        CompoundTag tag = right
                ? player.getShoulderEntityRight()
                : player.getShoulderEntityLeft();

        if (tag.isEmpty() || !tag.getString("id").equals("geckos:gecko")) {
            return false;
        }

        EntityType.create(tag, player.level()).ifPresent(entity -> {
            if (entity instanceof GeckoEntity gecko) {
                double forward = 0.6;
                double yawRad = Math.toRadians(player.getYRot());

                double x = player.getX() - Math.sin(yawRad) * forward;
                double z = player.getZ() + Math.cos(yawRad) * forward;
                double y = player.getY();

                gecko.moveTo(x, y, z, player.getYRot(), 0);
                player.level().addFreshEntity(gecko);

                gecko.setHealth(gecko.getMaxHealth());
                gecko.setNoAi(false);
                gecko.setInvulnerable(false);
                gecko.setOnGround(true);
                gecko.setDeltaMovement(0, 0, 0);

                gecko.setShoulderCooldown(100);
            }
        });

        try {
            var method = Player.class.getDeclaredMethod(
                    right ? "setShoulderEntityRight" : "setShoulderEntityLeft",
                    CompoundTag.class
            );
            method.setAccessible(true);
            method.invoke(player, new CompoundTag());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    private static boolean dropHead(ServerPlayer player) {
        CompoundTag tag = player.getData(ModAttachments.HEAD_GECKO.get());

        if (tag.isEmpty()) {
            return false;
        }

        EntityType.create(tag, player.level()).ifPresent(entity -> {
            if (entity instanceof GeckoEntity gecko) {
                gecko.readAdditionalSaveData(tag);

                gecko.moveTo(
                        player.getX(),
                        player.getY() + 1,
                        player.getZ(),
                        player.getYRot(),
                        0
                );

                gecko.setOnHead(false);
                gecko.setShoulderCooldown(100);

                player.level().addFreshEntity(gecko);
            }
        });

        player.setData(ModAttachments.HEAD_GECKO.get(), new CompoundTag());
        SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
        return true;
    }
}