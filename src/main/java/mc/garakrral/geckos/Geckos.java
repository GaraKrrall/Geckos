/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos;

import mc.garakrral.geckos.attachment.ModAttachments;
import mc.garakrral.geckos.block.ModBlocks;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.item.ModItems;
import mc.garakrral.geckos.item.group.ModItemGroups;
import mc.garakrral.geckos.util.function.ClientStartFunctions;
import mc.garakrral.geckos.util.function.ServerStartFunctions;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(Geckos.MODID)
public class Geckos {
    public static final String MODID = "geckos";
    private static final Logger LOGGER = LogUtils.getLogger();

    public Geckos(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(Geckos::commonSetup);

        ModItemGroups.TABS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        NeoForge.EVENT_BUS.register(Geckos.class);

        ServerStartFunctions.registerConfig(modContainer);
        ModAttachments.register(modEventBus);
        ClientStartFunctions.registerConfigScreen(modContainer);
    }

    private static void commonSetup(final FMLCommonSetupEvent event) {
        ServerStartFunctions.printInfoMessage();
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        ServerStartFunctions.printInfoMessage();
    }
}
