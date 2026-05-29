package mc.garakrral.geckos.gui.screen;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.ModConfig;
import mc.garakrral.geckos.attachment.ModAttachments;
import mc.garakrral.geckos.entity.client.keybind.KeyBindings;
import mc.garakrral.geckos.entity.custom.GeckoEntity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.EntityHitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = Main.MODID, value = Dist.CLIENT)
public class NeoForgeGameClientBusEvents {

    private static final ResourceLocation BOOK_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/gui/gecko_hud/gecko_paper.png");

    @SubscribeEvent
    public static void render(RenderGuiEvent.Post e) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;
        if (mc.screen != null) return;
        if (!ModConfig.SHOW_HUD.get()) return;

        if (!(mc.hitResult instanceof EntityHitResult hit)) return;
        if (!(hit.getEntity() instanceof GeckoEntity gecko)) return;
        if (!gecko.isTame()) return;
        if (!(gecko.getOwner() instanceof Player owner) || owner != mc.player) return;

        Player player = mc.player;
        GuiGraphics g = e.getGuiGraphics();

        int width = mc.getWindow().getGuiScaledWidth();
        int height = mc.getWindow().getGuiScaledHeight();

        boolean sleeping = gecko.isSleepingGecko();
        boolean babyGecko = gecko.isBaby();
        List<Component> tooltip = new ArrayList<>();

        if (ModConfig.SHOW_GECKO_NAME.get() && gecko.hasCustomName()) {
            tooltip.add(gecko.getCustomName().copy().withStyle(ChatFormatting.WHITE));
            tooltip.add(Component.literal("────────").withStyle(ChatFormatting.DARK_GRAY));
        }

        tooltip.add(line("Carry on Head", KeyBindings.HEAD.getTranslatedKeyMessage().getString(), !sleeping && !babyGecko && player.getData(ModAttachments.HEAD_GECKO.get()).isEmpty()));
        tooltip.add(line("Carry Right", KeyBindings.RIGHT.getTranslatedKeyMessage().getString(), !sleeping && !babyGecko && player.getShoulderEntityRight().isEmpty()));
        tooltip.add(line("Carry Left", KeyBindings.LEFT.getTranslatedKeyMessage().getString(), !sleeping && !babyGecko && player.getShoulderEntityLeft().isEmpty()));

        if (sleeping && ModConfig.SHOW_SLEEP_TEXT.get()) {
            tooltip.add(Component.literal("Gecko is sleeping").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        if (babyGecko) {
            tooltip.add(Component.literal("Gecko is baby").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }

        int drawX = ModConfig.HUD_X.get();
        int drawY = ModConfig.HUD_Y.get();

        if (ModConfig.SHOW_BACKGROUND.get()) {
            drawX = Math.min(drawX, width - 128);
            drawY = Math.min(drawY, height - 130);
            drawX = Math.max(0, drawX);
            drawY = Math.max(0, drawY);

            g.pose().pushPose();
            g.pose().scale(0.5F, 0.5F, 1F);

            g.blit(
                    BOOK_TEXTURE,
                    (int) (drawX / 0.5F),
                    (int) (drawY / 0.5F),
                    0,
                    0,
                    256,
                    260,
                    256,
                    260
            );

            int textX = (int) (drawX / 0.5F) + 32;
            int yy = (int) (drawY / 0.5F) + 40;

            for (Component c : tooltip) {
                g.drawString(mc.font, c, textX + 1, yy + 1, 0x000000, false);
                g.drawString(mc.font, c, textX, yy, 0x5A3A22, false);
                yy += 24;
            }

            g.pose().popPose();
        } else {
            int longest = 0;
            for (Component c : tooltip) {
                longest = Math.max(longest, mc.font.width(c));
            }
            int tooltipW = longest + 12;
            int tooltipH = tooltip.size() * 10 + 8;

            drawX = Math.min(drawX, width - tooltipW);
            drawY = Math.min(drawY, height - tooltipH);
            drawX = Math.max(0, drawX);
            drawY = Math.max(0, drawY);

            int yy = drawY;
            for (Component c : tooltip) {
                g.drawString(mc.font, c, drawX, yy, 0xFFFFFF);
                yy += 10;
            }
        }
    }

    private static Component line(String text, String key, boolean enabled) {
        Component c = Component.literal(text + " [" + key + "]");
        if (enabled) return c;

        return c.copy().withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }
}