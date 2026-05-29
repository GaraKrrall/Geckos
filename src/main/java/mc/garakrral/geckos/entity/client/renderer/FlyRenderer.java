package mc.garakrral.geckos.entity.client.renderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

import mc.garakrral.geckos.Main;
import mc.garakrral.geckos.entity.client.model.FlyModel;
import mc.garakrral.geckos.entity.custom.FlyEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;

public class FlyRenderer extends MobRenderer<FlyEntity, FlyModel<FlyEntity>> {
    public FlyRenderer(EntityRendererProvider.Context context) {
        super(context, new FlyModel<>(context.bakeLayer(FlyModel.LAYER_LOCATION)), 0.25f);
    }

    @NotNull
    @Override
    public ResourceLocation getTextureLocation(FlyEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Main.MODID, "textures/entity/fly/fly.png");
    }

    @Override
    public void render(FlyEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
