package io.screret.github.juicesandsodas.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import java.util.Random;
import java.util.Set;

public class FruitBlobFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<FruitBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> blobParts(instance).apply(instance, FruitBlobFoliagePlacer::new));

    public FruitBlobFoliagePlacer(FeatureSpread p_i241995_1_, FeatureSpread p_i241995_2_, int p_i241995_3_) {
        super(p_i241995_1_, p_i241995_2_, p_i241995_3_);
    }

    @Override
    protected void createFoliage(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int trunkHeight, FoliagePlacer.Foliage treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int i, MutableBoundingBox blockBox) {
        for (int j = i; j >= i - foliageHeight; --j) {
            int k = Math.max(radius + treeNode.radiusOffset() - 1 - j / 2, 0);
            this./*generate*/createFoliage(world, random, config, treeNode.radiusOffset(), treeNode, k, j, leaves, blockBox);
            BlockState core = config.leavesProvider.getState(random, treeNode.foliagePos());
            if (core.getBlock() instanceof FruitLeavesBlock) {
                core = core.setValue(LeavesBlock.DISTANCE, 1).setValue(LeavesBlock.PERSISTENT, true);
            }
            world.setBlock(treeNode.foliagePos(), core, 19);
        }
    }
}
