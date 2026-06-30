/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.util.function;

import mc.garakrral.geckos.client.gui.screen.GeckoConfigScreen;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Client-only startup helpers used during mod bootstrap.
 *
 * <p>This class is isolated from common code so GUI-related references remain on the client side.
 * Any call site that reaches into this class should first ensure the runtime distribution is
 * actually {@link Dist#CLIENT}.
 */
@OnlyIn(Dist.CLIENT)
public class ClientStartFunctions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientStartFunctions.class);

    /**
     * Registers the mod configuration screen factory used by NeoForge's mod list UI.
     *
     * @param container mod container that owns the extension point registration
     */
    public static void registerConfigScreen() {
        if (FMLLoader.getDist() == Dist.CLIENT) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory(
                            (mc, parent) -> GeckoConfigScreen.create(parent)
                    )
            );
        }
    }
}
