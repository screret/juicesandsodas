package io.screret.github.juicesandsodas.plants;

import io.screret.github.juicesandsodas.init.ModStuff;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LemonTree extends Tree {

   @Nullable
   @Override
   public ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
      return ModFeatures.LEMON_TREE;
   }

}
