package mc.garakrral.geckos.client.gui.screen;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.GeckosConfig;
import mc.garakrral.geckos.resources.GeckoResourceLocation;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import java.util.List;

@OnlyIn(Dist.CLIENT)
public class HudEditScreen extends Screen {

    private static final ResourceLocation BOOK_TEXTURE =  GeckoResourceLocation.getFromResources(GeckoResourceLocation.Resources.GECKO_PAPER_OLD);

    private int x;
    private int y;
    private boolean dragging;
    private int dragX;
    private int dragY;

    public HudEditScreen() {
        super(Component.literal("HUD Editor"));
        x = GeckosConfig.HUD_X.get();
        y = GeckosConfig.HUD_Y.get();
    }

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

    @Override
    public boolean mouseDragged(double mx, double my, int b, double dx, double dy) {
        if (dragging) {
            x = Math.max(0, Math.min((int) mx - dragX, width - 128));
            y = Math.max(0, Math.min((int) my - dragY, height - 130));
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseReleased(double mx, double my, int b) {
        dragging = false;
        return true;
    }

    @Override
    public void render(GuiGraphics g, int mx, int my, float pt) {
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