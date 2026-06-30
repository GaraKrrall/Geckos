package mc.garakrral.geckos;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;


@Mod.EventBusSubscriber(modid = Geckos.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GeckosConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.BooleanValue SHOW_GECKO_NAME = BUILDER.comment("Show gecko name in HUD").define("show_gecko_name", true);
    public static final ForgeConfigSpec.BooleanValue SHOW_BACKGROUND = BUILDER.define("show_background", true);
    public static final ForgeConfigSpec.BooleanValue SHOW_SLEEP_TEXT = BUILDER.define("show_sleep_text", true);
    public static final ForgeConfigSpec.BooleanValue SHOW_HUD = BUILDER.define("show_hud", true);
    public static final ForgeConfigSpec.IntValue HUD_X = BUILDER.defineInRange("hud_x", 472, 0, 10000);
    public static final ForgeConfigSpec.IntValue HUD_Y = BUILDER.defineInRange("hud_y", 313, 0, 10000);

   public static final ForgeConfigSpec SPEC = BUILDER.build();

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
    }
}
