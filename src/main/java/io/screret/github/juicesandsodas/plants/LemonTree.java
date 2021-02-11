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

        
   @Override
   protected Feature<? extends BaseTreeFeatureConfig> getFeature(Random random)
   {
      return Features.LEMON_TREE;
   }
}
