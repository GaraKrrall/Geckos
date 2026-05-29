package mc.garakrral.geckos;

import mc.garakrral.geckos.gui.screen.GeckoConfigScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class MainClient {
    @SubscribeEvent
    public static void clientSetup(FMLClientSetupEvent event) {

    }

    public static void initConfigScreen(ModContainer container) {
        container.registerExtensionPoint(
                IConfigScreenFactory.class,
                (mc,parent) -> GeckoConfigScreen.create(parent)
        );
    }
}
