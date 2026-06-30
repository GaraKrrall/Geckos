/*
 *

 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *

 */

package mc.garakrral.geckos;

import mc.garakrral.geckos.block.ModBlocks;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.item.group.ModItemGroups;
import mc.garakrral.geckos.util.function.ClientStartFunctions;
import mc.garakrral.geckos.util.function.ServerStartFunctions;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.slf4j.Logger;

/**

 * Primary mod entry point discovered by Forge through the {@link Mod} annotation.
 */
@Mod(Geckos.MODID)
public class Geckos {
    public static final String MODID = "geckos";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Geckos() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModContainer container = ModList.get().getModContainerById(MODID).orElseThrow();

        ServerStartFunctions.registerPackets();
        modEventBus.addListener(Geckos::commonSetup);

        ModItemGroups.TABS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, GeckosConfig.SPEC);

        MinecraftForge.EVENT_BUS.addListener(Geckos::onServerStarting);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ClientStartFunctions::registerConfigScreen);

    }

    private static void commonSetup(final net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent event) {
        ServerStartFunctions.printInfoMessage();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerStartFunctions.printInfoMessage();
    }
}
