package io.screret.github.juicesandsodas.plants.features;

import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.ModBlocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.*;
import net.minecraftforge.registries.ForgeRegistries;

public class Features {
	public static final Feature<BaseTreeFeatureConfig> LEMON_TREE = register("lemon_tree", new BasicTreeFeature.Builder().altLeaves(ModBlocks.LEMON_LEAVES.getDefaultState()).create());

	private static <C extends IFeatureConfig, F extends Feature<C>> F register(String key, F value) {
		value.setRegistryName(Base.MODID, key);
		ForgeRegistries.FEATURES.register(value);
		return value;
	}
}
