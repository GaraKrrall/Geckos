/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.renderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import mc.garakrral.geckos.client.model.FlyModel;
import mc.garakrral.geckos.entity.animal.FlyEntity;
import mc.garakrral.geckos.resources.GeckoResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer responsible for drawing fly entities.
 *
 * <p>The fly renderer is intentionally simple because the texture is fixed and the model does not
 * require special scaling or visibility rules beyond what the base renderer already provides.
 */
public class FlyRenderer extends MobRenderer<FlyEntity, FlyModel<FlyEntity>> {
    /**
     * Creates a fly renderer backed by the baked fly model layer.
     *
     * @param context renderer creation context
     */
    public FlyRenderer(EntityRendererProvider.Context context) {
        super(context, new FlyModel<>(context.bakeLayer(FlyModel.LAYER_LOCATION)), 0.25f);
    }

    /**
     * Returns the static texture used for all fly entities.
     *
     * @param entity fly instance being rendered
     * @return resource location of the fly texture
     */
    @NotNull
    @Override
    public ResourceLocation getTextureLocation(FlyEntity entity) {
        return GeckoResourceLocation.getFromResources(GeckoResourceLocation.Resources.FLY);
    }

    /**
     * Delegates rendering to the base implementation.
     *
     * @param entity fly entity being rendered
     * @param entityYaw interpolated body yaw
     * @param partialTicks frame partial tick value
     * @param poseStack pose stack used for transformations
     * @param buffer render buffer source
     * @param packedLight packed lightmap value
     */
    @Override
    public void render(FlyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
