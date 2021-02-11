package io.screret.github.juicesandsodas.plants.features;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.Tree;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Random;

public abstract class TreeDefaultConfig extends Tree
{
    @Override
    @Nullable
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean hasFlowers)
    {
        return null;
    }

    protected abstract Feature<? extends BaseTreeFeatureConfig> getFeature(Random random);

    @Override
    public boolean attemptGrowTree(ServerWorld world, ChunkGenerator generator, BlockPos pos, BlockState state, Random random)
    {
        Feature<BaseTreeFeatureConfig> feature = (Feature<BaseTreeFeatureConfig>)this.getFeature(random);
        if (feature == null)
        {
            return false;
        }
        else
        {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);
            if (feature.generate(world, generator, random, pos, Features.OAK.config))
            {
                return true;
            }
            else
            {
                world.setBlockState(pos, state, 4);
                return false;
            }
        }
    }
}