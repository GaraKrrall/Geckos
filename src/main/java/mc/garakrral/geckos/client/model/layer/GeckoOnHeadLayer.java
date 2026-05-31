/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.model.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

import mc.garakrral.geckos.attachment.ModAttachments;
import mc.garakrral.geckos.client.resources.GeckoResourceLocation;
import mc.garakrral.geckos.entity.ModEntities;
import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GeckoOnHeadLayer<T extends Player> extends RenderLayer<T, PlayerModel<T>> {
    private final GeckoModel model;

    public GeckoOnHeadLayer(RenderLayerParent<T, PlayerModel<T>> parent, EntityModelSet models) {
        super(parent);
        this.model = new GeckoModel(models.bakeLayer(GeckoModel.GECKO_LAYER_LOCATION));
    }

    @Override
    public void render(PoseStack pose, MultiBufferSource buffer, int light, T player, float limbSwing, float limbSwingAmount, float partialTick, float age, float yaw, float pitch) {
        CompoundTag tag = player.getData(ModAttachments.HEAD_GECKO.get());

        if (tag.isEmpty()) {return;}

        GeckoEntity gecko = new GeckoEntity(ModEntities.GECKO.get(), player.level());

        gecko.readAdditionalSaveData(tag);
        pose.pushPose();
        getParentModel().head.translateAndRotate(pose);
        pose.translate(0F, -1.84F, 0F);
        pose.scale(0.9F, 0.9F, 0.9F);

        GeckoVariants variant = gecko.getGeckoVariant();
        ResourceLocation tex = GeckoResourceLocation.GECKO_RESOURCE_LOCATION_MAP.getOrDefault(variant, GeckoResourceLocation.GECKO_RESOURCE_LOCATION_MAP.get(GeckoVariants.GREEN));
        VertexConsumer vc = buffer.getBuffer(model.renderType(tex));

        model.renderOnShoulder(pose, vc, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        pose.popPose();
    }
}