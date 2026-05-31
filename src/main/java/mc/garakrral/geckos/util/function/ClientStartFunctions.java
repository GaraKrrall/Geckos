/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.function;

import mc.garakrral.geckos.gui.screen.GeckoConfigScreen;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@OnlyIn(Dist.CLIENT)
public class ClientStartFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientStartFunctions.class);

    public static void registerConfigScreen(ModContainer container) {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            container.registerExtensionPoint(IConfigScreenFactory.class, (mc, parent)
                    -> GeckoConfigScreen.create(parent));
        }
    }
}
