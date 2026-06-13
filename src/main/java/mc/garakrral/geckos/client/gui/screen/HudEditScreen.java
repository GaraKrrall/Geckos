/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.gui.screen;

import mc.garakrral.geckos.GeckosConfig;
import mc.garakrral.geckos.resources.GeckoResourceLocation;
import mc.garakrral.geckos.resources.GeckoResourceLocation.Resources;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Simple draggable editor screen for repositioning the gecko HUD.
 *
 * <p>The screen previews the HUD background, lets the user drag it within screen bounds, and saves
 * the resulting coordinates back into the mod config when the user confirms.
 */
@OnlyIn(Dist.CLIENT)
public class HudEditScreen extends Screen {

    private static final ResourceLocation BOOK_TEXTURE = GeckoResourceLocation.getFromResources(Resources.GECKO_PAPER_OLD);

    private int x;
    private int y;
    private boolean dragging;
    private int dragX;
    private int dragY;

    /**
     * Creates the HUD editor screen and loads the current saved HUD position.
     */
    public HudEditScreen() {
        super(Component.literal("HUD Editor"));
        x = GeckosConfig.HUD_X.get();
        y = GeckosConfig.HUD_Y.get();
    }

    /**
     * Builds the editor widgets, including the save button.
     */
    @Override
    protected void init() {
        addRenderableWidget(Button.builder(Component.literal("Save"), b -> {
            GeckosConfig.HUD_X.set(x);
            GeckosConfig.HUD_Y.set(y);
            GeckosConfig.HUD_X.save();
            GeckosConfig.HUD_Y.save();
            onClose();
        }).bounds(width / 2 - 40, height - 28, 80, 20).build());
    }

    /**
     * Starts dragging the preview when the user clicks inside it.
     *
     * @param mx mouse X coordinate
     * @param my mouse Y coordinate
     * @param button mouse button id
     * @return {@code true} if the click was consumed by the draggable preview
     */
    @Override
    public boolean mouseClicked(double mx, double my, int button) {
        int w = 128;
        int h = 130;

        if (mx >= x && mx <= x + w && my >= y && my <= y + h) {
            dragging = true;
            dragX = (int) mx - x;
            dragY = (int) my - y;
            return true;
        }
        return super.mouseClicked(mx, my, button);
    }

    /**
     * Updates the preview position while dragging is active.
     *
     * @param mx current mouse X coordinate
     * @param my current mouse Y coordinate
     * @param b mouse button id
     * @param dx X delta since the previous drag event
     * @param dy Y delta since the previous drag event
     * @return {@code true} if the drag event was consumed
     */
    @Override
    public boolean mouseDragged(double mx, double my, int b, double dx, double dy) {
        if (dragging) {
            x = Math.max(0, Math.min((int) mx - dragX, width - 128));
            y = Math.max(0, Math.min((int) my - dragY, height - 130));
            return true;
        }
        return false;
    }

    /**
     * Ends an active drag operation.
     *
     * @param mx release mouse X coordinate
     * @param my release mouse Y coordinate
     * @param b mouse button id
     * @return always {@code true} because releasing ends the drag interaction
     */
    @Override
    public boolean mouseReleased(double mx, double my, int b) {
        dragging = false;
        return true;
    }

    /**
     * Renders the screen background, draggable preview, and preview text.
     *
     * @param g GUI graphics context
     * @param mx mouse X coordinate
     * @param my mouse Y coordinate
     * @param pt partial tick value
     */
    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
        renderBlurredBackground(pt);
        super.render(g, mx, my, pt);

        g.pose().pushPose();
        g.pose().scale(0.5F, 0.5F, 1F);

        g.blit(
                BOOK_TEXTURE,
                (int) (x / 0.5F),
                (int) (y / 0.5F),
                0,
                0,
                256,
                260,
                256,
                260
        );

        List<Component> preview = List.of(
                Component.literal("Gecko HUD"),
                Component.literal("Drag this").withStyle(ChatFormatting.YELLOW),
                Component.literal("Position: " + x + ", " + y).withStyle(ChatFormatting.GRAY)
        );

        int textX = (int) (x / 0.5F) + 32;
        int yy = (int) (y / 0.5F) + 40;

        for (Component c : preview) {
            g.drawString(font, c, textX + 1, yy + 1, 0x000000, false);
            g.drawString(font, c, textX, yy, 0x5A3A22, false);
            yy += 24;
        }

        g.pose().popPose();
    }
}
