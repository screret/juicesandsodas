package io.screret.github.juicesandsodas.properties.block.blender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.screret.github.juicesandsodas.blocks.BlenderBlock;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import io.screret.github.juicesandsodas.tileentities.IBlenderRod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.DualBrightnessCallback;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMerger;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlenderOnModel<T extends TileEntity & IBlenderRod> extends TileEntityRenderer<T>{
	private ModelRenderer blenderOnModel;
	private ModelRenderer case_model;
	private ModelRenderer windows;
	private ModelRenderer lid;
	private ModelRenderer rod;

	public static final ResourceLocation TEXTURE = new ResourceLocation("minecraft", "textures/blocks/oak_planks.png");

	public BlenderOnModel(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
		int textureWidth = 16;
		int textureHeight = 16;

		blenderOnModel = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		blenderOnModel.setRotationPoint(0.0F, 24.0F, 0.0F);


		case_model = new ModelRenderer(textureWidth, textureHeight, 0, 0);
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

		windows = new ModelRenderer(textureWidth, textureHeight, 32, 0);
		windows.setRotationPoint(0.0F, -5.0F, 0.0F);
		blenderOnModel.addChild(windows);
		windows.setTextureOffset(4, 0).addBox(4.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, false);
		windows.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, -5.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);
		windows.setTextureOffset(1, 0).addBox(-5.0F, -4.0F, -4.0F, 1.0F, 8.0F, 8.0F, 0.0F, false);
		windows.setTextureOffset(0, 0).addBox(-4.0F, -4.0F, 4.0F, 8.0F, 8.0F, 1.0F, 0.0F, false);

		lid = new ModelRenderer(textureWidth, textureHeight, 0, 0);
		lid.setRotationPoint(0.0F, -9.75F, 0.0F);
		blenderOnModel.addChild(lid);
		lid.addBox(-4.0F, -0.25F, -4.0F, 8.0F, 1.0F, 8.0F, 0.0F, false);

		rod = new ModelRenderer(textureWidth, textureHeight, 16, 0);
		rod.setRotationPoint(0.0F, 5.35F, 0.0F);
		lid.addChild(rod);
		rod.setTextureOffset(3, 3).addBox(-1.0F, -8.6F, -1.0F, 2.0F, 10.0F, 2.0F, 0.0F, true);
		rod.addBox(1.0F, 0.4F, -1.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(2.0F, 0.4F, 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(-3.0F, 0.4F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		rod.addBox(-3.0F, 0.4F, 0.0F, 2.0F, 1.0F, 1.0F, 0.0F, false);
	}



	private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer glass, ModelRenderer case_model, ModelRenderer rod, float lidAngle, int combinedLightIn, int combinedOverlayIn) {
		rod.rotateAngleX = -(lidAngle * ((float)Math.PI / 2F));
		rod.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		case_model.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
		glass.render(matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
	}

	@Override
	public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		World world = tileEntityIn.getWorld();
		boolean flag = world != null;
		BlockState blockstate = flag ? tileEntityIn.getBlockState() : Blocks.CHEST.getDefaultState().with(ChestBlock.FACING, Direction.SOUTH);
		Block block = blockstate.getBlock();
		if (block instanceof BlenderBlock) {
			matrixStackIn.push();
			matrixStackIn.translate(0.5D, 0.5D, 0.5D);
			matrixStackIn.translate(-0.5D, -0.5D, -0.5D);
			TileEntityMerger.ICallbackWrapper<? extends BlenderTile> icallbackwrapper;
			icallbackwrapper = TileEntityMerger.ICallback::func_225537_b_;

			float f1 = icallbackwrapper.apply(BlenderBlock.getLidRotationCallback(tileEntityIn)).get(partialTicks);
			f1 = 1.0F - f1;
			f1 = 1.0F - f1 * f1 * f1;
			int i = icallbackwrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
			RenderMaterial rendermaterial = Atlases.CHEST_MATERIAL;
			IVertexBuilder ivertexbuilder = rendermaterial.getBuffer(bufferIn, RenderType::getEntityCutout);

			this.renderModels(matrixStackIn, ivertexbuilder, this.windows, this.case_model, this.rod, f1, i, combinedOverlayIn);

			matrixStackIn.pop();
		}
	}
}