package mc.garakrral.geckos.entity.client.model.layer;

import mc.garakrral.geckos.entity.client.model.GeckoModel;
import mc.garakrral.geckos.entity.client.renderer.GeckoRenderer;
import mc.garakrral.geckos.entity.custom.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariant;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

@OnlyIn(Dist.CLIENT)
public class GeckoOnShoulderLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final GeckoModel model;

    public GeckoOnShoulderLayer(RenderLayerParent<T, PlayerModel<T>> parent, EntityModelSet modelSet) {
        super(parent);
        this.model = new GeckoModel(modelSet.bakeLayer(GeckoModel.LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T player,
                       float limbSwing, float limbSwingAmount, float partialTick,
                       float ageInTicks, float netHeadYaw, float headPitch) {
        renderGecko(poseStack, buffer, light, player, true);
        renderGecko(poseStack, buffer, light, player, false);
    }

    private void renderGecko(PoseStack poseStack, MultiBufferSource buffer, int light, T player, boolean left) {
        CompoundTag tag = left ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        if (tag.isEmpty() || !tag.getString("id").equals("geckos:gecko")) return;

        poseStack.pushPose();
        poseStack.translate(left ? 0.4F : -0.4F, player.isCrouching() ? -1.3F : -1.5F, 0.0F);

        GeckoVariant variant = getVariantFromNBT(tag);
        ResourceLocation texture = GeckoRenderer.GECKO_VARIANT_RESOURCE_LOCATION_MAP.getOrDefault(
                variant, GeckoRenderer.GECKO_VARIANT_RESOURCE_LOCATION_MAP.get(GeckoVariant.GREEN)
        );

        VertexConsumer vc = buffer.getBuffer(variant == GeckoVariant.PHANTOM
                ? RenderType.entityTranslucent(texture)
                : model.renderType(texture));

        model.renderOnShoulder(
                poseStack,
                vc,
                light,
                OverlayTexture.NO_OVERLAY,
                variant == GeckoVariant.PHANTOM ? 0x33FFFFFF : 0xFFFFFFFF
        );

        poseStack.popPose();
    }

    private GeckoVariant getVariantFromNBT(CompoundTag tag) {
        if (tag.contains("Variant", 3)) {
            return GeckoVariant.byId(tag.getInt("Variant"));
        }
        return GeckoVariant.GREEN;
    }
}