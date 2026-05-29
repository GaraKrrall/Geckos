package mc.garakrral.geckos.gui.screen;

import mc.garakrral.geckos.ModConfig;
import mc.garakrral.geckos.entity.client.keybind.KeyBindings;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

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

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Gecko Name"), ModConfig.SHOW_GECKO_NAME.get())
                .setDefaultValue(true)
                .setSaveConsumer(ModConfig.SHOW_GECKO_NAME::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Tooltip Background"), ModConfig.SHOW_BACKGROUND.get())
                .setDefaultValue(true)
                .setSaveConsumer(ModConfig.SHOW_BACKGROUND::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Sleep Text"), ModConfig.SHOW_SLEEP_TEXT.get())
                .setDefaultValue(true)
                .setSaveConsumer(ModConfig.SHOW_SLEEP_TEXT::set)
                .build());

        hud.addEntry(e.startBooleanToggle(Component.literal("Show Gecko HUD"), ModConfig.SHOW_HUD.get())
                .setDefaultValue(true)
                .setSaveConsumer(ModConfig.SHOW_HUD::set)
                .build());

        builder.setSavingRunnable(() -> {
            ModConfig.HUD_X.save();
            ModConfig.HUD_Y.save();
            ModConfig.SHOW_GECKO_NAME.save();
            ModConfig.SHOW_BACKGROUND.save();
            ModConfig.SHOW_SLEEP_TEXT.save();
            ModConfig.SHOW_HUD.save();
        });

        return builder.build();
    }
}