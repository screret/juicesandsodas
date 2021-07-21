package io.screret.github.juicesandsodas.properties.block.blender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class BlenderTileRenderer extends TileEntityRenderer<BlenderTile> {

	final static ModelBlender model = new ModelBlender();
	public static final ResourceLocation IRON_TEX = new ResourceLocation("minecraft", "textures/block/iron_block.png");

	public float rotationDegrees = 0.0F;

	public BlenderTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	@Override
	public void render(BlenderTile tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
		matrixStack.pushPose(); // push the current transformation matrix + normals matrix

		if(tileEntity.isBlending()){
			model.rotatingParts.yRot = (float)Math.toRadians(rotationDegrees);
			rotationDegrees -= 2;
		}
		//model.askThings(buffer, tileEntity);
		IVertexBuilder woodBuffer = buffer.getBuffer(model.renderType(IRON_TEX));
		model.renderToBuffer(matrixStack, woodBuffer, combinedLight, combinedOverlay, 1, 1, 1, 1);
		matrixStack.popPose();
	}


	/*@Override
	public void render(BlenderTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
		World world = tileEntityIn.getWorld();
		TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEX);

		boolean flag = world != null;
		BlockState blockstate = flag ? tileEntityIn.getBlockState() : Registration.BLENDER.get().getDefaultState();
		Block block = blockstate.getBlock();
		if (block instanceof BlenderBlock) {
			matrixStackIn.push();
			TileEntityMerger.ICallbackWrapper<? extends BlenderTile> icallbackwrapper;
			icallbackwrapper = TileEntityMerger.ICallback::acceptNone;

			float f1 = icallbackwrapper.apply(BlenderBlock.getLidRotationCallback(tileEntityIn)).get(partialTicks);
			f1 -= 1.0F;
			f1 = 1.0F - f1 * f1 * f1;
			int i = icallbackwrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
			IVertexBuilder ivertexbuilder = TEXTURE.getBuffer(bufferIn, RenderType::getEntityTranslucent);

			this.renderModels(matrixStackIn, ivertexbuilder, this.chassis, this.rod, f1, i);

			matrixStackIn.pop();
		}
	}*/
}