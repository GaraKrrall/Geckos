package mc.garakrral.geckos;

import mc.garakrral.geckos.attachment.ModAttachments;
import net.minecraft.client.renderer.entity.EntityRenderers;

import mc.garakrral.geckos.block.ModBlocks;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.entity.client.renderer.FlyRenderer;
import mc.garakrral.geckos.entity.client.renderer.GeckoRenderer;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.item.group.ModItemGroups;
import mc.garakrral.geckos.entity.client.keybind.KeyBindings;
import mc.garakrral.geckos.entity.client.model.layer.GeckoOnShoulderLayer;
import mc.garakrral.geckos.entity.client.model.layer.GeckoOnHeadLayer;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.resources.PlayerSkin;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Main.MODID)
public class Main {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "geckos";
    // Directly reference a slf4j logger
//  private static final Logger LOGGER = LogUtils.getLogger();
    //Global Logger
        public static final Logger LOGGER =
                LogUtils.getLogger();


    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Main(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(Main::commonSetup);

        ModItemGroups.TABS.register(modEventBus);

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        ModEntities.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(Main.class);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.CLIENT, ModConfig.SPEC);

        ModAttachments.register(modEventBus);

        if (FMLLoader.getDist() == Dist.CLIENT) {
            MainClient.initConfigScreen(modContainer);
        }
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        info();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
       info();
    }

    private static void info() {
        LOGGER.info("Say hi for geckos!");
    }

    @SuppressWarnings("removal")
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            EntityRenderers.register(ModEntities.GECKO.get(), GeckoRenderer::new);
            EntityRenderers.register(ModEntities.FLY.get(), FlyRenderer::new);
        }

        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(KeyBindings.DISMOUNT_KEY);
            event.register(KeyBindings.HEAD);
            event.register(KeyBindings.LEFT);
            event.register(KeyBindings.RIGHT);
            event.register(KeyBindings.HUD_EDITOR);
            event.register(KeyBindings.CARRY_GECKO);
        }
        @SubscribeEvent
        public static void addLayers(EntityRenderersEvent.AddLayers event) {
            for (PlayerSkin.Model skin : event.getSkins()) {
                PlayerRenderer renderer = event.getSkin(skin);

                LOGGER.info("Adding layer for {}", skin);

                if (renderer != null) {
                    renderer.addLayer(new GeckoOnShoulderLayer<>(
                            renderer,
                            event.getEntityModels()
                    ));

                    renderer.addLayer(
                            new GeckoOnHeadLayer<>(
                                    renderer,
                                    event.getEntityModels()
                            ));

                    LOGGER.info("Added Gecko Layer {}", skin);
                }
            }
        }
    }
}
