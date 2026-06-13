/*
 *
 * Copyright (c) 2026 GaraKrral
 *
 * Licensed under the GPLv3 License.
 * See LICENSE file in the project root for full license information.
 *
 */

package mc.garakrral.geckos.client.renderer;

import mc.garakrral.geckos.resources.GeckoResourceLocation;
import mc.garakrral.geckos.client.model.GeckoModel;
import mc.garakrral.geckos.entity.animal.GeckoEntity;
import mc.garakrral.geckos.entity.variant.GeckoVariants;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

/**
 * Renderer responsible for drawing gecko entities in the world.
 *
 * <p>In addition to selecting the correct texture based on variant, this renderer scales baby
 * geckos and suppresses body rendering for the phantom variant by overriding visibility logic.
 */
public class GeckoRenderer extends MobRenderer<GeckoEntity, GeckoModel<GeckoEntity>> {
    /**
     * Creates a gecko renderer using the baked model layer provided by the rendering context.
     *
     * @param context renderer creation context containing model sets and render helpers
     */
    public GeckoRenderer(EntityRendererProvider.Context context) {
        super(context, new GeckoModel<>(context.bakeLayer(GeckoModel.GECKO_LAYER_LOCATION)), 0.25f);
    }

    /**
     * Resolves the texture to use for the supplied gecko instance.
     *
     * @param entity gecko being rendered
     * @return resource location of the active gecko texture
     */
    @NotNull
    @Override
    public ResourceLocation getTextureLocation(@NotNull GeckoEntity entity) {
        return GeckoResourceLocation.getGeckoTextureFromResources(entity);
    }

    /**
     * Renders the gecko with extra baby scaling before delegating to the base mob renderer.
     *
     * @param gecko entity being rendered
     * @param yaw interpolated body yaw
     * @param partialTicks frame partial tick value
     * @param poseStack pose stack used for transformations
     * @param buffer render buffer source
     * @param packedLight packed lightmap value
     */
    @Override
    public void render(GeckoEntity gecko, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (gecko.isBaby()) {
            poseStack.scale(0.5F, 0.5F, 0.5F);
        }

        super.render(gecko, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    /**
     * Determines whether the gecko body should be treated as visible by the renderer.
     *
     * @param entity gecko entity being evaluated
     * @return {@code true} unless the entity uses the phantom variant
     */
    @Override
    protected boolean isBodyVisible(GeckoEntity entity) {
        GeckoVariants variant = entity.getGeckoVariant();

        return variant != GeckoVariants.PHANTOM;
    }
}
