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

import com.mojang.logging.LogUtils;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

public class ServerStartFunctions {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void printInfoMessage() {
        LOGGER.info("Say hi for geckos!");
    }

    public static void registerConfig(ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, GeckosConfig.SPEC);
    }
}
