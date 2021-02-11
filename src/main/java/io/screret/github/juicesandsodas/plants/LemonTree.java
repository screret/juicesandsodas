package io.screret.github.juicesandsodas.plants;

import io.screret.github.juicesandsodas.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;

import java.util.Random;

public class LemonTree extends Tree {

    BaseTreeFeatureConfig config = new BaseTreeFeatureConfig.Builder(
            new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()),
            new SimpleBlockStateProvider(ModBlocks.LEMON_LEAVES.getDefaultState()),
            new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3),
            new StraightTrunkPlacer(4, 2, 0),
            new TwoLayerFeature(1, 0, 1))
            .setIgnoreVines()
                .build();

    @org.jetbrains.annotations.Nullable
    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
        return new ConfiguredFeature<BaseTreeFeatureConfig, Feature<BaseTreeFeatureConfig>>(TreeFeature.TREE, config);
    }
}