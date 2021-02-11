package io.screret.github.juicesandsodas.plants;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.registries.ForgeRegistries;

public class Features {
    public static final Feature<BaseTreeFeatureConfig> LEMON_TREE = register("lemon_tree", new BasicTreeFeature.Builder().altLeaves(ModBlocks.LEMON_LEAVES.defaultBlockState()).create());
    
    private static <C extends IFeatureConfig, F extends Feature<C>> F register(String key, F value)
	{
		value.setRegistryName(new ResourceLocation(Base.MODID, key));
		ForgeRegistries.FEATURES.register(value);
		return value;
	}
{
