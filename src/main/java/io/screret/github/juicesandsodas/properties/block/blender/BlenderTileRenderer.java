package io.screret.github.juicesandsodas.properties.block.blender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import io.screret.github.juicesandsodas.util.Config;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.client.model.data.EmptyModelData;

import java.util.Random;

public class BlenderTileRenderer extends TileEntityRenderer<BlenderTile> {

	public static final ResourceLocation TEX = new ResourceLocation(Base.MODID, "textures/entity/blender.png");

	public BlenderTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
		super(rendererDispatcherIn);
	}

	private void add(IVertexBuilder renderer, MatrixStack stack, float x, float y, float z, float u, float v) {
		renderer.pos(stack.getLast().getMatrix(), x, y, z)
				.color(1.0f, 1.0f, 1.0f, 1.0f)
				.tex(u, v)
				.lightmap(0, 240)
				.normal(1, 0, 0)
				.endVertex();
	}

	private static float diffFunction(long time, long delta, float scale) {
		long dt = time % (delta * 2);
		if (dt > delta) {
			dt = 2*delta - dt;
		}
		return dt * scale;
	}

	@Override
	public void render(BlenderTile tileEntity, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {

		TextureAtlasSprite sprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(TEX);
		IVertexBuilder builder = buffer.getBuffer(RenderType.getTranslucent());
		long time = System.currentTimeMillis();

		Random rnd = new Random(tileEntity.getPos().getX() * 337L + tileEntity.getPos().getY() * 37L + tileEntity.getPos().getZ() * 13L);


		double speed = Config.ROTATION_SPEED.get();
		float angle = (time / (int)speed) % 360;
		Quaternion rotation = Vector3f.YP.rotationDegrees(angle);


		matrixStack.push();
		matrixStack.translate(.5, .5, .5);
		matrixStack.rotate(rotation);
		matrixStack.scale(-1, -1, -1);
		matrixStack.translate(-.5, -.5, -.5);

		add(builder, matrixStack, 0.25f, 0.0625f, 0.75F, sprite.getMinU(), sprite.getMinV());
		add(builder, matrixStack, 0.1875f, 0, 0.1875f, sprite.getMaxU(), sprite.getMinV());
		add(builder, matrixStack, 0.1875f, 0.5625f, 0.1875f, sprite.getMaxU(), sprite.getMaxV());
		add(builder, matrixStack, 0.75f, 0.0625f, 0.1875f, sprite.getMinU(), sprite.getMaxV());
		add(builder, matrixStack, 0.1875f, 0.0625f, 0.1875f, sprite.getMinU(), sprite.getMaxV());
		add(builder, matrixStack, 0.25f, 0.0625f, 0.1875f, sprite.getMinU(), sprite.getMaxV());

		add(builder, matrixStack, 0.5f, 0.1875f, 0.3125f, sprite.getMinU(), sprite.getMaxV());
		add(builder, matrixStack, 0.4375f, 0.1875f, 0.4375f, sprite.getMaxU(), sprite.getMaxV());
		add(builder, matrixStack, 0.5f, 0.1875f, 0.5625f, sprite.getMaxU(), sprite.getMinV());
		add(builder, matrixStack, 0.4375f, 0.1875f, 0.3125f, sprite.getMinU(), sprite.getMinV());
		add(builder, matrixStack, 0.4375f, 0.1875f, 0.625f, sprite.getMinU(), sprite.getMinV());

		matrixStack.pop();

		matrixStack.push();

		matrixStack.translate(-.5, 1, -.5);
		BlockRendererDispatcher blockRenderer = Minecraft.getInstance().getBlockRendererDispatcher();
		BlockState state = Registration.BLENDER.get().getDefaultState();
		blockRenderer.renderBlock(state, matrixStack, buffer, combinedLight, combinedOverlay, EmptyModelData.INSTANCE);

		matrixStack.pop();
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
			icallbackwrapper = TileEntityMerger.ICallback::func_225537_b_;

			float f1 = icallbackwrapper.apply(BlenderBlock.getLidRotationCallback(tileEntityIn)).get(partialTicks);
			f1 -= 1.0F;
			f1 = 1.0F - f1 * f1 * f1;
			int i = icallbackwrapper.apply(new DualBrightnessCallback<>()).applyAsInt(combinedLightIn);
			IVertexBuilder ivertexbuilder = TEXTURE.getBuffer(bufferIn, RenderType::getEntityTranslucent);

			this.renderModels(matrixStackIn, ivertexbuilder, this.chassis, this.rod, f1, i);

			matrixStackIn.pop();
		}
	}*/

	private void renderModels(MatrixStack matrixStackIn, IVertexBuilder bufferIn, ModelRenderer chassis, ModelRenderer rod, float rodAngle, int combinedLightIn) {
		rod.rotateAngleY = -(rodAngle * ((float)Math.PI / 2F));
		chassis.render(matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY);
		rod.render(matrixStackIn, bufferIn, combinedLightIn, OverlayTexture.NO_OVERLAY);
	}
}