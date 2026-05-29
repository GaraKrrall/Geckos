package mc.garakrral.geckos.event;

import mc.garakrral.geckos.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Main.MODID)
public class NeoForgeEventBusEvents {

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) return;

        CompoundTag left = player.getShoulderEntityLeft();
        CompoundTag right = player.getShoulderEntityRight();

        if (isGecko(left) || isGecko(right)) {

            try {
                var field = Player.class.getDeclaredField("timeEntitySatOnShoulder");
                field.setAccessible(true);
                field.setLong(player, player.level().getGameTime());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static boolean isGecko(CompoundTag tag) {
        return !tag.isEmpty() && tag.getString("id").equals("geckos:gecko");
    }
}
