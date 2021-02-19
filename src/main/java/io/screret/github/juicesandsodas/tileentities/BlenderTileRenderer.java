package io.screret.github.juicesandsodas.tileentities;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BlenderTileRenderer extends TileEntityRenderer<BlenderTile> {


    public BlenderTileRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    /**
     * render the tile entity - called every frame while the tileentity is in view of the player
     *
     * @param tile the associated tile entity
     * @param partialTicks    the fraction of a tick that this frame is being rendered at - used to interpolate frames between
     *                        ticks, to make animations smoother.  For example - if the frame rate is steady at 80 frames per second,
     *                        this method will be called four times per tick, with partialTicks spaced 0.25 apart, (eg) 0, 0.25, 0.5, 0.75
     * @param matrixStack     the matrixStack is used to track the current view transformations that have been applied - i.e translation, rotation, scaling
     *                        it is needed for you to render the view properly.
     * @param renderBuffers    the buffer that you should render your model to
     * @param combinedLight   the blocklight + skylight value for the tileEntity.  see http://greyminecraftcoder.blogspot.com/2014/12/lighting-18.html (outdated, but the concepts are still valid)
     * @param combinedOverlay value for the "combined overlay" which changes the render based on an overlay texture (see OverlayTexture class).
     *                        Used by vanilla for (1) red tint when a living entity takes damage, and (2) "flash" effect for creeper when ignited
     *                        CreeperRenderer.getOverlayProgress()
     */
    @Override
    public void render(BlenderTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                       int combinedLight, int combinedOverlay) {
        matrixStack.push(); // push the current transformation matrix + normals matrix

        Model model = new BlenderOnModel();

        // if you need to manually change the combinedLight you can use these helper functions...
        int blockLight = LightTexture.getLightBlock(combinedLight);
        int skyLight = LightTexture.getLightSky(combinedLight);
        int repackedValue = LightTexture.packLight(blockLight, skyLight);

        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0.0D, (double) -1.501F, 0.0D);
        IVertexBuilder renderBuffer = renderBuffers.getBuffer(model.getRenderType(BlenderOnModel.TEXTURE));
        model.render(matrixStack, renderBuffer, combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F); // white, fully opaque
        matrixStack.pop();
    }

    // this should be true for tileentities which render globally (no render bounding box), such as beacons.
    @Override
    public boolean isGlobalRenderer(BlenderTile tile)
    {
        return true;
    }

    private static final Logger LOGGER = LogManager.getLogger();
}