package mc.garakrral.geckos.client.model;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;

import mc.garakrral.geckos.Geckos;
import mc.garakrral.geckos.client.animation.FlyAnimations;
import mc.garakrral.geckos.entity.animal.FlyEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public class FlyModel<T extends FlyEntity> extends HierarchicalModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(Geckos.MODID, "fly"), "main");

    private final ModelPart root;
    private final ModelPart wing;
    private final ModelPart body;
    private final ModelPart wing2;

    public FlyModel(ModelPart root) {
        this.root = root;
        this.wing = root.getChild("wing");
        this.body = root.getChild("body");
        this.wing2 = root.getChild("wing2");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition wing = partdefinition.addOrReplaceChild("wing", CubeListBuilder.create().texOffs(0, 7).addBox(-0.001F, 0.0436F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-0.001F, 0.0436F, 2.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 20.0F, -2.0F));

        PartDefinition cube_r1 = wing.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.999F, 0.0436F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -4.0F, -3.0F, 4.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition wing2 = partdefinition.addOrReplaceChild("wing2", CubeListBuilder.create().texOffs(0, 7).addBox(-0.001F, 0.0436F, -2.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(0, 7).addBox(-0.001F, 0.0436F, 0.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, 20.0F, -1.0F, 0.0F, 3.1416F, 0.0F));

        PartDefinition cube_r2 = wing2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 7).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.999F, 0.0436F, -1.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animate(entity.flyAnimationState, FlyAnimations.fly, ageInTicks, 1f);
    }

    @Override
    public void renderToBuffer(PoseStack p_170625_, VertexConsumer p_170626_, int p_170627_, int p_170628_, float p_170629_, float p_170630_, float p_170631_, float p_170632_) {
        this.root.render(p_170625_, p_170626_, p_170627_, p_170628_, p_170629_, p_170630_, p_170631_, p_170632_);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}

