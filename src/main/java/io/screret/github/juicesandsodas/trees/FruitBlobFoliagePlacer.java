package io.screret.github.juicesandsodas.trees;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.screret.github.juicesandsodas.init.Registration;
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
    public static final Codec<FruitBlobFoliagePlacer> CODEC = RecordCodecBuilder.create(instance -> blobParts(instance).apply(instance, FruitBlobFoliagePlacer::new));

    public FruitBlobFoliagePlacer(FeatureSpread spread1, FeatureSpread spread2, int height) {
        super(spread1, spread2, height);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return Registration.FRUIT_PLACER;
    }

    @Override
    protected void createFoliage(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig config, int trunkHeight, FoliagePlacer.Foliage treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int i, MutableBoundingBox blockBox) {
        for (int j = i; j >= i - foliageHeight; --j) {
            int k = Math.max(radius + treeNode.radiusOffset() - 1 - j / 2, 0);
            this.placeLeavesRow(world, random, config, treeNode.foliagePos(), k, leaves, j, treeNode.doubleTrunk(), blockBox);
            BlockState core = config.leavesProvider.getState(random, treeNode.foliagePos());
            if (core.getBlock() instanceof FruitLeavesBlock) {
                core = core.setValue(LeavesBlock.DISTANCE, 1).setValue(LeavesBlock.PERSISTENT, true);
            }
            world.setBlock(treeNode.foliagePos(), core, 19);
        }
    }
}
