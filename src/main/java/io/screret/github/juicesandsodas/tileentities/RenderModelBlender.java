package io.screret.github.juicesandsodas.tileentities;


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.Random;

/**
 * This class is adapted from part of the Botania Mod, thanks to Vazkii and WillieWillus
 * Get the Source Code in github
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * Features of the Hourglass model:
 * 1) Parts of the hourglass are transparent (use alpha blending)
 * 2) The hourglass is animated - it moves (wiggles) up and down, and rotates
 * 3) The sand in the hourglass is rendered with different colours; the top colour fades and the bottom colour intensifies
 *    as the sand in the top chamber runs down into the bottom chamber
 */

public class RenderModelBlender  {

    final static BlenderOnModel model = new BlenderOnModel();

    public static void renderUsingModel(BlenderTile tile, float partialTicks, MatrixStack matrixStack, IRenderTypeBuffer renderBuffers,
                                        int combinedLight, int combinedOverlay) {

        matrixStack.push(); // push the current transformation matrix + normals matrix

        // The model is defined centred on [0,0,0], so if we drew it at the current render origin, its centre would be
        // at the corner of the block, sunk halfway into the ground and overlapping into the adjacent blocks.
        // We want it to hover above the centre of the hopper base, so we need to translate up and across to the desired position
        final Vector3d TRANSLATION_OFFSET = new Vector3d(0.5, 1.5, 0.5);
        matrixStack.translate(TRANSLATION_OFFSET.x, TRANSLATION_OFFSET.y, TRANSLATION_OFFSET.z); // translate

        // we use an animation timer to manipulate the render, with a "random" offset added to the animation timer based on
        // the world position
        // This ensures that adjacent hourglasses don't animate in lock step, which looks weird
        double animationTicks = AnimationTickCounter.getTotalElapsedTicksInGame() + partialTicks;
        animationTicks += new Random(tile.getPos().hashCode()).nextInt(Integer.MAX_VALUE);

        AnimationState animationState = new AnimationState(animationTicks);

        // add a small amount of wiggle to the hourglass' position
        Vector3f wiggle = getWiggle(animationTicks);
        matrixStack.translate(wiggle.getX(), wiggle.getY(), wiggle.getZ());

        // Vanilla applies the following transformations to standard Models, set USE_ENTITY_MODEL_TRANSFORMATIONS to true
        //  to apply those.
        // i.e. all standard Vanilla models are defined to have their feet on the ground for a model y coordinate of 24 units
        //   when rendered at y = 0.  (see mbe80 and http://greyminecraftcoder.blogspot.com/2020/03/minecraft-model-1144.html)
        boolean USE_ENTITY_MODEL_TRANSFORMATIONS = true;

        if (USE_ENTITY_MODEL_TRANSFORMATIONS) {
            matrixStack.scale(-1, -1, 1);
            matrixStack.translate(0.0D, (double) -1.501F, 0.0D);
        }

        IVertexBuilder renderBuffer1 = renderBuffers.getBuffer(model.getRenderType(GLASS_TEXTURE));
        IVertexBuilder renderBuffer2 = renderBuffers.getBuffer(model.getRenderType(PLANK_TEXTURE));
        IVertexBuilder renderBuffer3 = renderBuffers.getBuffer(model.getRenderType(IRON_TEXTURE));
        model.render(matrixStack, renderBuffer1, combinedLight, combinedOverlay, 255, 255, 255, 255);
        model.render(matrixStack, renderBuffer2, combinedLight, combinedOverlay, 255, 255, 255, 255);
        model.render(matrixStack, renderBuffer3, combinedLight, combinedOverlay, 255, 255, 255, 255, animationState.flipRotationDegrees);
        matrixStack.pop();
    }

    /**
     * Add a small amount of wiggle to the hourglass' position, based on the animation timer
     * @param animationTicks animation ticks including partialTicks
     * @return
     */
    private static Vector3f getWiggle(double animationTicks) {
        final int TICKS_PER_SECOND = 20;
        final double X_WIGGLE_CYCLE_SECONDS = 0.7;
        final double Y_WIGGLE_CYCLE_SECONDS = 2.3;
        final double Z_WIGGLE_CYCLE_SECONDS = 0.76;
        final double X_WIGGLE_CYCLE_TICKS = X_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
        final double Y_WIGGLE_CYCLE_TICKS = Y_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
        final double Z_WIGGLE_CYCLE_TICKS = Z_WIGGLE_CYCLE_SECONDS * TICKS_PER_SECOND;
        final float XZ_WIGGLE_AMPLITUDE = 0.01F;
        final float Y_WIGGLE_AMPLITUDE = 0.05F;
        final double RADIANS_PER_CYCLE = 2*Math.PI;

        double wiggle_x = XZ_WIGGLE_AMPLITUDE * Math.cos((animationTicks / X_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
        double wiggle_y = Y_WIGGLE_AMPLITUDE  * Math.sin((animationTicks / Y_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
        double wiggle_z = XZ_WIGGLE_AMPLITUDE * Math.sin((animationTicks / Z_WIGGLE_CYCLE_TICKS) * RADIANS_PER_CYCLE);
        return new Vector3f((float)wiggle_x, (float)wiggle_y, (float)wiggle_z);
    }

    /**
     * Helper class to convert the animation ticks into animation parameters of the hourglass (sand fullness,
     *   and end-over-end rotation when the sand expires)
     */
    static class AnimationState {
        public AnimationState (double animationTicks) {
            float cycleOffset = (float)(animationTicks % FULL_CYCLE_DURATION_TICKS);

            // the animation cycle is:
            // 1) Hourglass is upright.  Sand runs out for SAND_DEPLETION_TICKS
            // 2) Hourglass spends FLIP_DURATION_TICKS to flip over by 180 degrees
            // 3) Hourglass is upside down.  Sand runs out for SAND_DEPLETION_TICKS
            // 4) Hourglass spends FLIP_DURATION_TICKS to flip over by 180 degrees to be upright again.

            if (cycleOffset <= SAND_DEPLETION_TICKS) {
                flipRotationDegrees = 0;
            }
        }

        public float flipRotationDegrees;  // used to flip the hourglass end-over-end (when the sand runs out)

        private final float FLIP_DURATION_SECONDS = 0.2F;  // how long does it take to flip the hourglass end-over-end?
        private final float SAND_DEPLETION_SECONDS = 17.8F; // how long does it take the sand to
        private final float HALF_CYCLE_DURATION_SECONDS = FLIP_DURATION_SECONDS + SAND_DEPLETION_SECONDS;
        private final float FULL_CYCLE_DURATION_SECONDS = 2*HALF_CYCLE_DURATION_SECONDS;

        private final int TICKS_PER_SECOND = 20;
        private final float SAND_DEPLETION_TICKS = SAND_DEPLETION_SECONDS * TICKS_PER_SECOND;
        private final float FULL_CYCLE_DURATION_TICKS = FULL_CYCLE_DURATION_SECONDS * TICKS_PER_SECOND;
    }

    public static final ResourceLocation GLASS_TEXTURE = new ResourceLocation("minecraft:textures/blocks/glass.png");
    public static final ResourceLocation PLANK_TEXTURE = new ResourceLocation("minecraft:textures/blocks/oak_planks.png");
    public static final ResourceLocation IRON_TEXTURE = new ResourceLocation("minecraft:textures/blocks/iron_block.png");

}