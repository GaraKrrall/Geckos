package mc.garakrral.geckos.client.packet;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.entity.animal.GeckoEntity;

import com.mojang.logging.LogUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.Supplier;

public record GeckoDismountPacket() {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void encode(GeckoDismountPacket msg, FriendlyByteBuf buf) {
    }

    public static GeckoDismountPacket decode(FriendlyByteBuf buf) {
        return new GeckoDismountPacket();
    }

    public static void handle(GeckoDismountPacket payload, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;

            if (!dropShoulder(player, true)) {
                dropShoulder(player, false);
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private static boolean dropShoulder(ServerPlayer player, boolean right) {
        /*CompoundTag tag = right ? player.getShoulderEntityRight() : player.getShoulderEntityLeft();

        if (tag.isEmpty() || !tag.getString("id").equals("geckos:gecko")) {
            return false;
        }

        Optional<Entity> created = EntityType.create(tag, player.level());
        if (created.isPresent() && created.get() instanceof GeckoEntity gecko) {
            double forward = 0.6D;
            double yawRad = Math.toRadians(player.getYRot());

            double x = player.getX() - Math.sin(yawRad) * forward;
            double z = player.getZ() + Math.cos(yawRad) * forward;
            double y = player.getY();

            gecko.moveTo(x, y, z, player.getYRot(), 0.0F);
            player.level().addFreshEntity(gecko);

            gecko.setHealth(gecko.getMaxHealth());
            gecko.setNoAi(false);
            gecko.setInvulnerable(false);
            gecko.setOnGround(true);
            gecko.setDeltaMovement(0, 0, 0);
            gecko.setShoulderCooldown(100);
        }

        LOGGER.info("before R={}", player.getShoulderEntityRight());

        boolean ok = player.setEntityOnShoulder(new CompoundTag());

        LOGGER.info("result={}", ok);
        LOGGER.info("after R={}", player.getShoulderEntityRight());*/
        return true;
    }
}
