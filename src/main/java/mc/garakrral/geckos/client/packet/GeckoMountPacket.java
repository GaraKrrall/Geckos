package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record GeckoMountPacket(int entityId, MountType mountType) {

    private static final String HEAD_KEY = Geckos.MODID + ":head_gecko";

    public enum MountType {
        HEAD,
        LEFT,
        RIGHT
    }

    public static void encode(GeckoMountPacket msg, FriendlyByteBuf buf) {
        buf.writeInt(msg.entityId());
        buf.writeEnum(msg.mountType());
    }

    public static GeckoMountPacket decode(FriendlyByteBuf buf) {
        return new GeckoMountPacket(buf.readInt(), buf.readEnum(MountType.class));
    }

    public static void handle(GeckoMountPacket payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            Entity e = player.level().getEntity(payload.entityId());
            if (!(e instanceof GeckoEntity gecko)) return;

            if (!gecko.isTame() || gecko.getOwner() != player) return;
            if (gecko.isSleepingGecko()) return;
            if (gecko.isBaby()) return;

            CompoundTag save = new CompoundTag();
            gecko.saveWithoutId(save);
            save.putString("id", "geckos:gecko");

            switch (payload.mountType()) {
                case HEAD -> {
                    CompoundTag currentHead = player.getPersistentData().getCompound(HEAD_KEY);
                    if (!currentHead.isEmpty()) return;

                    save.putBoolean("OnHead", true);
                    player.getPersistentData().put(HEAD_KEY, save);

                    // SyncHeadGeckoPacket'i ayrıca Forge'a portladıysan burada client sync at.
                    // SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
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
        ctx.get().setPacketHandled(true);
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