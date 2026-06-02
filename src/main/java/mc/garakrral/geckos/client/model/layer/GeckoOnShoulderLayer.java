/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.model.layer;

import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.resources.GeckoResourceLocation;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

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
        this.model = new GeckoModel(modelSet.bakeLayer(GeckoModel.GECKO_LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int light, T player, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        renderGecko(poseStack, buffer, light, player, true);
        renderGecko(poseStack, buffer, light, player, false);
    }

    private void renderGecko(PoseStack poseStack, MultiBufferSource buffer, int light, T player, boolean left) {
        CompoundTag tag = left ? player.getShoulderEntityLeft() : player.getShoulderEntityRight();
        if (tag.isEmpty() || !tag.getString("id").equals("geckos:gecko")) return;

        poseStack.pushPose();
        poseStack.translate(left ? 0.4F : -0.4F, player.isCrouching() ? -1.3F : -1.5F, 0.0F);

        GeckoVariants variant = getVariantFromNBT(tag);
        ResourceLocation texture = GeckoResourceLocation.getGeckoTextureFromResources(variant);

        VertexConsumer vc = buffer.getBuffer(variant == GeckoVariants.PHANTOM ? RenderType.entityTranslucent(texture) : model.renderType(texture));

        model.renderOnShoulder(poseStack, vc, light, OverlayTexture.NO_OVERLAY, variant == GeckoVariants.PHANTOM ? 0x33FFFFFF : 0xFFFFFFFF);

        poseStack.popPose();
    }

    private GeckoVariants getVariantFromNBT(CompoundTag tag) {
        if (tag.contains("Variant", 3)) {
            return GeckoVariants.byId(tag.getInt("Variant"));
        }
        return GeckoVariants.GREEN;
    }
}