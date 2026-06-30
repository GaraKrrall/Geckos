package mc.garakrral.geckos.client.gui.screen;

import mc.garakrral.geckos.GeckosConfig;
import mc.garakrral.geckos.client.keybind.KeyBindings;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoConfigScreen {

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.literal("Geckos Config"))
                .setDoesConfirmSave(false);

        ConfigEntryBuilder e = builder.entryBuilder();
        ConfigCategory hud = builder.getOrCreateCategory(Component.literal("HUD"));

        hud.addEntry(e.startTextDescription(Component.empty()
                .append(Component.literal("Press "))
                .append(Component.literal("["))
                .append(KeyBindings.HUD_EDITOR.getTranslatedKeyMessage())
                .append(Component.literal("]"))
                .append(Component.literal(" in-game to move the Gecko HUD"))
                .withStyle(ChatFormatting.GRAY)).build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Gecko Name"), GeckosConfig.SHOW_GECKO_NAME.get())
                .setDefaultValue(true)
                .setSaveConsumer(GeckosConfig.SHOW_GECKO_NAME::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Tooltip Background"), GeckosConfig.SHOW_BACKGROUND.get())
                .setDefaultValue(true)
                .setSaveConsumer(GeckosConfig.SHOW_BACKGROUND::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Sleep Text"), GeckosConfig.SHOW_SLEEP_TEXT.get())
                .setDefaultValue(true)
                .setSaveConsumer(GeckosConfig.SHOW_SLEEP_TEXT::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Gecko HUD"), GeckosConfig.SHOW_HUD.get())
                .setDefaultValue(true)
                .setSaveConsumer(GeckosConfig.SHOW_HUD::set)
                .build());

        builder.setSavingRunnable(() -> {
            GeckosConfig.HUD_X.save();
            GeckosConfig.HUD_Y.save();
            GeckosConfig.SHOW_GECKO_NAME.save();
            GeckosConfig.SHOW_BACKGROUND.save();
            GeckosConfig.SHOW_SLEEP_TEXT.save();
            GeckosConfig.SHOW_HUD.save();
        });

        return builder.build();
    }
}