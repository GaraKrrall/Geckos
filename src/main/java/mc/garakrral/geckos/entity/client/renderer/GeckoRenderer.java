package mc.garakrral.geckos.entity.client.renderer;

import java.util.Map;

import mc.garakrral.geckos.entity.variant.type.GeckoType;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.client.model.GeckoModel;
import mc.garakrral.geckos.entity.custom.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariant;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class GeckoRenderer extends MobRenderer<GeckoEntity, GeckoModel<GeckoEntity>> {
    public static final Map<GeckoVariant, ResourceLocation> GECKO_VARIANT_RESOURCE_LOCATION_MAP =
            Util.make(Maps.newEnumMap(GeckoVariant.class), map -> {
                map.put(GeckoVariant.BLUE,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_blue.png"));
                map.put(GeckoVariant.PINK,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_pink.png"));
                map.put(GeckoVariant.BROWN,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_brown.png"));
                map.put(GeckoVariant.GREEN,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_green.png"));
                map.put(GeckoVariant.RED,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_red.png"));
                map.put(GeckoVariant.PHANTOM,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_brown.png"));
                map.put(GeckoVariant.NETHER_BASALT,
                        ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/gecko/gecko_brown.png"));

});

    public GeckoRenderer(EntityRendererProvider.Context context) {
        super(context, new GeckoModel<>(context.bakeLayer(GeckoModel.LAYER_LOCATION)), 0.25f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(GeckoEntity entity) {
        return GECKO_VARIANT_RESOURCE_LOCATION_MAP.get(entity.getGeckoVariant());
    }

    @Override
    public void render(GeckoEntity gecko, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        GeckoType geckoType = gecko.getGeckoType();
        if (geckoType != GeckoType.NORMAL) return;

        if (gecko.isBaby()) {
            poseStack.scale(0.5F,0.5F,0.5F);
        }

        super.render(
                gecko,
                yaw,
                partialTicks,
                poseStack,
                buffer,
                packedLight
        );
    }

    @Override
    protected boolean isBodyVisible(GeckoEntity entity) {
        GeckoVariant variant = entity.getGeckoVariant();

        if (variant == GeckoVariant.PHANTOM) {
            return false;
        } else {
            return true;
        }
    }
}
