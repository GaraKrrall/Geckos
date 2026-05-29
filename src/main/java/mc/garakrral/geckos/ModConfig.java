package mc.garakrral.geckos;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@SuppressWarnings("removal")
@EventBusSubscriber(modid = Main.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue SHOW_GECKO_NAME = BUILDER.comment("Show gecko name in HUD").define("show_gecko_name", true);
    public static final ModConfigSpec.BooleanValue SHOW_BACKGROUND = BUILDER.define("show_background", true);
    public static final ModConfigSpec.BooleanValue SHOW_SLEEP_TEXT = BUILDER.define("show_sleep_text", true);
    public static final ModConfigSpec.BooleanValue SHOW_HUD = BUILDER.define("show_hud", true);
    public static final ModConfigSpec.IntValue HUD_X = BUILDER.defineInRange("hud_x", 472, 0, 10000);
    public static final ModConfigSpec.IntValue HUD_Y = BUILDER.defineInRange("hud_y", 313, 0, 10000);

    static final ModConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}