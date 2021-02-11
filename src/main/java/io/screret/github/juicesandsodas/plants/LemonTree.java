package io.screret.github.juicesandsodas.plants;

import io.screret.github.juicesandsodas.plants.features.Features;
import io.screret.github.juicesandsodas.plants.features.TreeDefaultConfig;
import net.minecraft.world.gen.feature.*;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class LemonTree extends TreeDefaultConfig {


   @Override
   protected Feature<? extends BaseTreeFeatureConfig> getFeature(Random random)
   {
      return Features.LEMON_TREE;
   }

   @Nullable
   @Override
   protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random randomIn, boolean largeHive) {
      return null;
   }
}
