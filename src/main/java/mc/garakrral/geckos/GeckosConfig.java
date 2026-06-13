/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Declares the mod configuration specification used by NeoForge's config system.
 *
 * <p>All values in this class are defined statically so they can be registered once and then read
 * from anywhere in the codebase that needs HUD-related configuration. The options currently focus
 * on whether the HUD is visible and where it is positioned on screen.
 */
@SuppressWarnings("removal")
@EventBusSubscriber(modid = Geckos.MODID, bus = EventBusSubscriber.Bus.MOD)
public class GeckosConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_GECKO_NAME = BUILDER.comment("Show gecko name in HUD").define("show_gecko_name", true);
    public static final ModConfigSpec.BooleanValue SHOW_BACKGROUND = BUILDER.define("show_background", true);
    public static final ModConfigSpec.BooleanValue SHOW_SLEEP_TEXT = BUILDER.define("show_sleep_text", true);
    public static final ModConfigSpec.BooleanValue SHOW_HUD = BUILDER.define("show_hud", true);
    public static final ModConfigSpec.IntValue HUD_X = BUILDER.defineInRange("hud_x", 472, 0, 10000);
    public static final ModConfigSpec.IntValue HUD_Y = BUILDER.defineInRange("hud_y", 313, 0, 10000);

    public static final ModConfigSpec SPEC = BUILDER.build();

    /**
     * Receives config lifecycle notifications from NeoForge.
     *
     * <p>The method currently acts as a placeholder hook. Keeping it in place still documents the
     * intended extension point for future config reloading, validation, or cache-refresh logic.
     *
     * @param event configuration event fired when the mod config is loaded or reloaded
     */
    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {}
}
