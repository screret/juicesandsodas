package io.screret.github.juicesandsodas.properties.block.blender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class BlenderOnModel extends Model {
	private ModelRenderer blenderOnModel;
	private ModelRenderer case_model;
	private ModelRenderer windows;
	private ModelRenderer lid;
	private ModelRenderer rod;

	public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/blocks/oak_planks.png");

	public BlenderOnModel() {
		super(RenderType::getEntitySolid);
		int textureWidth = 16;
		int textureHeight = 16;


		this.getRenderType(TEXTURE);

		blenderOnModel = new ModelRenderer(this);
		blenderOnModel.setRotationPoint(0.0F, 24.0F, 0.0F);


		case_model = new ModelRenderer(this, 0, 0);
		case_model.setRotationPoint(0.0F, -6, 0.0F);
		blenderOnModel.addChild(case_model);
		case_model.addBox(-4.0F, -3F, 4.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		case_model.addBox(-5.0F, -3F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		case_model.addBox(-5.0F, -3F, 4.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.addBox(4.0F, -3F, 4.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.addBox(-5.0F, 5, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
		case_model.addBox(-5.0F, -3F, -5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.addBox(-4.0F, -3F, -5.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		case_model.addBox(4.0F, -3F, -5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.addBox(4.0F, -3F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

		windows = new ModelRenderer(this);
		windows.setRotationPoint(0.0F, -5.0F, 0.0F);
		blenderOnModel.addChild(windows);
		windows.setTextureOffset(4, 0).addBox(4.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, false);
		windows.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
		windows.setTextureOffset(1, 0).addBox(-5.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, false);
		windows.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, 4.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);

		lid = new ModelRenderer(this);
		lid.setRotationPoint(0.0F, -9.75F, 0.0F);
		blenderOnModel.addChild(lid);
		lid.addBox(-4.0F, -0.25F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

		rod = new ModelRenderer(this);
		rod.setRotationPoint(0.0F, 5.35F, 0.0F);
		lid.addChild(rod);
		rod.setTextureOffset(3, 3).addBox(-1.0F, -8.6F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		rod.addBox(1.0F, 0.4F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(2.0F, 0.4F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(-3.0F, 0.4F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(-3.0F, 0.4F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		blenderOnModel.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void render(MatrixStack matrixStack, IVertexBuilder renderBuffer, int combinedLight, int combinedOverlay, float r, float g, float b, float a, float rotationDegrees) {

		final float CONTAINER_RED = 1.0F;
		final float CONTAINER_GREEN = 1.0F;
		final float CONTAINER_BLUE = 1.0F;

		final float ALPHA_VALUE = 1.0F;

		rod.rotateAngleY = (float)Math.toRadians(rotationDegrees);

		case_model.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
		windows.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
		lid.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
		rod.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, CONTAINER_RED, CONTAINER_GREEN, CONTAINER_BLUE, ALPHA_VALUE);
	}
}