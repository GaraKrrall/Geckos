/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.renderer;

import mc.garakrral.geckos.client.resources.GeckoResourceLocation;
import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.entity.custom.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class GeckoRenderer extends MobRenderer<GeckoEntity, GeckoModel<GeckoEntity>> {
    public GeckoRenderer(EntityRendererProvider.Context context) {
        super(context, new GeckoModel<>(context.bakeLayer(GeckoModel.GECKO_LAYER_LOCATION)), 0.25f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(GeckoEntity entity) {
        return GeckoResourceLocation.GECKO_RESOURCE_LOCATION_MAP.get(entity.getGeckoVariant());
    }

    @Override
    public void render(GeckoEntity gecko, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (gecko.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        super.render(gecko, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected boolean isBodyVisible(GeckoEntity entity) {
        GeckoVariants variant = entity.getGeckoVariant();

        return variant != GeckoVariants.PHANTOM;
    }
}
