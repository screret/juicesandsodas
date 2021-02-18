package io.screret.github.juicesandsodas.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.screret.github.juicesandsodas.init.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

public class FruitBlobFoliagePlacer extends BlobFoliagePlacer {
    public static final Codec<FruitBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> {
        return func_236740_a_(instance).apply(instance, FruitBlobFoliagePlacer::new);
    });

    public FruitBlobFoliagePlacer(FeatureSpread p_i241995_1_, FeatureSpread p_i241995_2_, int p_i241995_3_) {
        super(p_i241995_1_, p_i241995_2_, p_i241995_3_);
    }

    @Override
    protected FoliagePlacerType<?> func_230371_a_() {
        return Registry.BLOB_PLACER;
    }

    @Override
    protected void generate(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int trunkHeight, FoliagePlacer.Foliage treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int i, MutableBoundingBox blockBox) {
        for (int j = i; j >= i --foliageHeight; --j) {
            int k = Math.max(radius + treeNode.getFoliageRadius() - 1 - j / 2, 0);
            this.generate(world, random, config, treeNode.getCenter(), k, leaves, j, treeNode.isGiantTrunk(), blockBox);
            BlockState core = config.leavesProvider.getBlockState(random, treeNode.getCenter());
            if (core.getBlock() instanceof FruitLeavesBlock) {
                core = core.with(LeavesBlock.DISTANCE, 1).with(LeavesBlock.PERSISTENT, true);
            }
            world.setBlockState(treeNode.getCenter(), core, 19);
        }
    }
}