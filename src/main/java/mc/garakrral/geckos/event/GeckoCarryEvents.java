package mc.garakrral.geckos.event;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.custom.GeckoEntity;
import mc.garakrral.geckos.entity.packet.GeckoDistancePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class GeckoCarryEvents {
    public static GeckoEntity carriedGecko;
    private static int oldLevel = -1;
    private static float oldProgress = -1F;

    @SubscribeEvent
    public static void mouseScroll(InputEvent.MouseScrollingEvent e) {
        Minecraft mc = Minecraft.getInstance();

        if (carriedGecko == null) return;
        if (!carriedGecko.isCarried()) return;

        e.setCanceled(true);

        float dist = carriedGecko.getCarryDistance();

        dist += (float) e.getScrollDeltaY() * 0.2F;
        dist = Mth.clamp(dist, GeckoEntity.MIN_CARRY_DISTANCE, GeckoEntity.MAX_CARRY_DISTANCE);

        carriedGecko.setCarryDistance(dist);

        PacketDistributor.sendToServer(
                new GeckoDistancePacket(
                        carriedGecko.getId(),
                        dist
                )
        );
    }

//    @SubscribeEvent
//    public static void onRenderOverlay(RenderGuiLayerEvent.Post e) {
//
//        Minecraft mc = Minecraft.getInstance();
//
//        if (mc.player == null) return;
//
//        if (carriedGecko != null && carriedGecko.isCarried()) {
//
//            if (oldLevel == -1) {
//                oldLevel = mc.player.experienceLevel;
//                oldProgress = mc.player.experienceProgress;
//            }
//
//            float min = GeckoEntity.MIN_CARRY_DISTANCE;
//            float max = GeckoEntity.MAX_CARRY_DISTANCE;
//
//            float dist = carriedGecko.getCarryDistance();
//
//            float progress = (dist - min) / (max - min);
//            progress = Mth.clamp(progress, 0F, 1F);
//
//            mc.player.experienceProgress = progress;
//            mc.player.experienceLevel = 0;
//
//        } else {
//
//            if (oldLevel != -1) {
//                mc.player.experienceLevel = oldLevel;
//                mc.player.experienceProgress = oldProgress;
//
//                oldLevel = -1;
//                oldProgress = -1F;
//            }
//        }
//    }
}
