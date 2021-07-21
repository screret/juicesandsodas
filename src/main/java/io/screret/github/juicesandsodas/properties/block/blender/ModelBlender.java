package io.screret.github.juicesandsodas.properties.block.blender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.math.vector.Vector3i;

public class ModelBlender extends Model {

    //public ModelRenderer staticParts;
    //public ModelRenderer clearParts;
    public ModelRenderer rotatingParts;

    //BlenderTile tile;
    public static final ResourceLocation IRON_TEX = new ResourceLocation("minecraft", "textures/block/iron_block.png");
    public static final ResourceLocation BROWN_GLASS_TEX = new ResourceLocation("minecraft", "textures/block/brown_stained_glass.png");
    public static final ResourceLocation WOOD_TEX = new ResourceLocation("minecraft", "textures/block/oak_planks.png");

    public ModelBlender() {
        super(RenderType::entityTranslucent);

        texWidth = 16;
        texHeight = 16;

        final float EXPANSION_AMOUNT = 0;

        /*final Vector3f BOTTOM_POS = new Vector3f(3, 0, 3);
        final Vector3i BOTTOM_SIZE = new Vector3i(10, 1, 10);

        final Vector3f TOP_POS = new Vector3f(3, 9, 3);
        final Vector3i TOP_SIZE = new Vector3i(10, 1, 10);

        staticParts = new ModelRenderer(this);
        staticParts.addBox(TOP_POS.x(), TOP_POS.y(), TOP_POS.z(),
                TOP_SIZE.getX(), TOP_SIZE.getY(), TOP_SIZE.getZ(),
                EXPANSION_AMOUNT);
        staticParts.addBox(BOTTOM_POS.x(), BOTTOM_POS.y(), BOTTOM_POS.z(),
                BOTTOM_SIZE.getX(), BOTTOM_SIZE.getY(), BOTTOM_SIZE.getZ(),
                EXPANSION_AMOUNT);
        staticParts.setPos(0, 0, 0);


        final Vector3f NORTH_WINDOW_POS = new Vector3f(4, 1, 3);
        final Vector3i NORTH_WINDOW_SIZE = new Vector3i(8, 8, 1);

        final Vector3f SOUTH_WINDOW_POS = new Vector3f(4, 1, 12);
        final Vector3i SOUTH_WINDOW_SIZE = new Vector3i(8, 8, 1);

        final Vector3f WEST_WINDOW_POS = new Vector3f(3, 1, 3);
        final Vector3i WEST_WINDOW_SIZE = new Vector3i(1, 8, 10);

        final Vector3f EAST_WINDOW_POS = new Vector3f(12, 1, 3);
        final Vector3i EAST_WINDOW_SIZE = new Vector3i(1, 8, 10);

        clearParts = new ModelRenderer(this);
        clearParts.addBox(NORTH_WINDOW_POS.x(), NORTH_WINDOW_POS.y(), NORTH_WINDOW_POS.z(),
                NORTH_WINDOW_SIZE.getX(), NORTH_WINDOW_SIZE.getY(), NORTH_WINDOW_SIZE.getZ(),
                EXPANSION_AMOUNT);
        clearParts.addBox(SOUTH_WINDOW_POS.x(), SOUTH_WINDOW_POS.y(), SOUTH_WINDOW_POS.z(),
                SOUTH_WINDOW_SIZE.getX(), SOUTH_WINDOW_SIZE.getY(), SOUTH_WINDOW_SIZE.getZ(),
                EXPANSION_AMOUNT);
        clearParts.addBox(WEST_WINDOW_POS.x(), WEST_WINDOW_POS.y(), WEST_WINDOW_POS.z(),
                WEST_WINDOW_SIZE.getX(), WEST_WINDOW_SIZE.getY(), WEST_WINDOW_SIZE.getZ(),
                EXPANSION_AMOUNT);
        clearParts.addBox(EAST_WINDOW_POS.x(), EAST_WINDOW_POS.y(), EAST_WINDOW_POS.z(),
                EAST_WINDOW_SIZE.getX(), EAST_WINDOW_SIZE.getY(), EAST_WINDOW_SIZE.getZ(),
                EXPANSION_AMOUNT);
        clearParts.setPos(0, 0, 0);
        */

        final Vector3f ROD_POS = new Vector3f(-1, 3, -1);
        final Vector3i ROD_SIZE = new Vector3i(2, 9, 2);

        final Vector3f SPIKE_NORTH_POS = new Vector3f(0, 3, -3);
        final Vector3i SPIKE_NORTH_SIZE = new Vector3i(1, 1, 1);

        final Vector3f SPIKE_SOUTH_POS = new Vector3f(-1, 3, 2);
        final Vector3i SPIKE_SOUTH_SIZE = new Vector3i(1, 1, 1);

        final Vector3f CONNECTOR_NORTH_POS = new Vector3f(0, 3, 1);
        final Vector3i CONNECTOR_NORTH_SIZE = new Vector3i(1, 1, 2);

        final Vector3f CONNECTOR_SOUTH_POS = new Vector3f(-1, 3, -3);
        final Vector3i CONNECTOR_SOUTH_SIZE = new Vector3i(1, 1, 2);

        rotatingParts = new ModelRenderer(this);
        rotatingParts.addBox(ROD_POS.x(), ROD_POS.y(), ROD_POS.z(),
                ROD_SIZE.getX(), ROD_SIZE.getY(), ROD_SIZE.getZ(),
                EXPANSION_AMOUNT);
        rotatingParts.addBox(SPIKE_NORTH_POS.x(), SPIKE_NORTH_POS.y(), SPIKE_NORTH_POS.z(),
                SPIKE_NORTH_SIZE.getX(), SPIKE_NORTH_SIZE.getY(), SPIKE_NORTH_SIZE.getZ(),
                EXPANSION_AMOUNT);
        rotatingParts.addBox(SPIKE_SOUTH_POS.x(), SPIKE_SOUTH_POS.y(), SPIKE_SOUTH_POS.z(),
                SPIKE_SOUTH_SIZE.getX(), SPIKE_SOUTH_SIZE.getY(), SPIKE_SOUTH_SIZE.getZ(),
                EXPANSION_AMOUNT);
        rotatingParts.addBox(CONNECTOR_NORTH_POS.x(), CONNECTOR_NORTH_POS.y(), CONNECTOR_NORTH_POS.z(),
                CONNECTOR_NORTH_SIZE.getX(), CONNECTOR_NORTH_SIZE.getY(), CONNECTOR_NORTH_SIZE.getZ(),
                EXPANSION_AMOUNT);
        rotatingParts.addBox(CONNECTOR_SOUTH_POS.x(), CONNECTOR_SOUTH_POS.y(), CONNECTOR_SOUTH_POS.z(),
                CONNECTOR_SOUTH_SIZE.getX(), CONNECTOR_SOUTH_SIZE.getY(), CONNECTOR_SOUTH_SIZE.getZ(),
                EXPANSION_AMOUNT);

        rotatingParts.setPos(8F, 0, 8F);
    }

    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        rotatingParts.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        //buffer = typeBuffer.getBuffer(renderType(WOOD_TEX));
        //staticParts.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        //buffer = typeBuffer.getBuffer(renderType(BROWN_GLASS_TEX));
        //clearParts.render(matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
