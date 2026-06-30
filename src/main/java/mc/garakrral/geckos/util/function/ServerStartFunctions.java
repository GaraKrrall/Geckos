/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.function;

import mc.garakrral.geckos.GeckosConfig;
import mc.garakrral.geckos.client.packet.ModPackets;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.slf4j.Logger;

/**
 * Shared startup helpers that are safe to invoke from common or server-side code paths.
 *
 * <p>The methods here intentionally avoid client-only references so they can be called from the mod
 * entry point regardless of whether the runtime is an integrated server or a dedicated server.
 */
public class ServerStartFunctions {
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Emits a simple startup log message for diagnostics and visibility during development.
     */
    public static void printInfoMessage() {
        LOGGER.info("Say hi for geckos!");
    }

    /**
     * Registers the mod's client config specification with the NeoForge config system.
     *
     * @param container mod container receiving the config registration
     */
    public static void registerConfig() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, GeckosConfig.SPEC);
    }

    public static void registerPackets() {
        ModPackets.register();
        LOGGER.info("Registering ModPackets");
        LOGGER.warn("The pick up geckos on head feature is not supported in Forge 1.20.1.");
    }
}
