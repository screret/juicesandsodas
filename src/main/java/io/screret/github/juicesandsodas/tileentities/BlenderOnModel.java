package io.screret.github.juicesandsodas.tileentities;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class BlenderOnModel extends EntityModel<Entity> {
	private ModelRenderer blenderOnModel;
	private ModelRenderer case_model;
	private ModelRenderer windows;
	private ModelRenderer lid;
	private ModelRenderer rod;

	public BlenderOnModel() {
		int textureWidth = 16;
		int textureHeight = 16;

		blenderOnModel = new ModelRenderer(this);
		blenderOnModel.setRotationPoint(0.0F, 24.0F, 0.0F);


		case_model = new ModelRenderer(this);
		case_model.setRotationPoint(0.0F, -6.7222F, 0.0F);
		blenderOnModel.addChild(case_model);
		case_model.setTextureOffset(0, 8).addBox(-4.0F, -3.2778F, 4.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 0).addBox(-5.0F, -3.2778F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);
		case_model.setTextureOffset(0, 1).addBox(-5.0F, -3.2778F, 4.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 1).addBox(4.0F, -3.2778F, 4.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 8).addBox(-5.0F, 5.7222F, -5.0F, 10.0F, 1.0F, 10.0F, 0.0F, false);
		case_model.setTextureOffset(0, 1).addBox(-5.0F, -3.2778F, -5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 8).addBox(-4.0F, -3.2778F, -5.0F, 8.0F, 1.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 1).addBox(4.0F, -3.2778F, -5.0F, 1.0F, 9.0F, 1.0F, 0.0F, false);
		case_model.setTextureOffset(0, 8).addBox(4.0F, -3.2778F, -4.0F, 1.0F, 1.0F, 8.0F, 0.0F, false);

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
		lid.setTextureOffset(0, 8).addBox(-4.0F, -0.25F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

		rod = new ModelRenderer(this);
		rod.setRotationPoint(0.0F, 5.35F, 0.0F);
		lid.addChild(rod);
		rod.setTextureOffset(3, 3).addBox(-1.0F, -8.6F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		rod.setTextureOffset(0, 1).addBox(1.0F, 0.4F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		rod.setTextureOffset(0, 1).addBox(2.0F, 0.4F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.setTextureOffset(0, 1).addBox(-3.0F, 0.4F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.setTextureOffset(0, 1).addBox(-3.0F, 0.4F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		blenderOnModel.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
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