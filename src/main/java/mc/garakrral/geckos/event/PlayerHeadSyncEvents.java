package mc.garakrral.geckos.event;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.packet.SyncHeadGeckoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@EventBusSubscriber(modid = Main.MODID)
public class PlayerHeadSyncEvents {

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;
        SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
    }

    @SubscribeEvent
    public static void onRespawn(PlayerEvent.PlayerRespawnEvent e) {
        if (!(e.getEntity() instanceof ServerPlayer player)) return;
        SyncHeadGeckoPacket.sendToTrackersAndSelf(player);
    }

    @SubscribeEvent
    public static void onStartTracking(PlayerEvent.StartTracking e) {
        if (!(e.getEntity() instanceof ServerPlayer viewer)) return;
        if (!(e.getTarget() instanceof ServerPlayer owner)) return;
        SyncHeadGeckoPacket.sendToViewer(viewer, owner);
    }
}