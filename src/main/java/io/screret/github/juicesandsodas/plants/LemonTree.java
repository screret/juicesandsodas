package io.screret.github.juicesandsodas.plants;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.ModFeatures;
import io.screret.github.juicesandsodas.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LemonTree extends Tree {

   @Nullable
   @Override
   public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
      return ModFeatures.LEMON;
   }

}
