package io.screret.github.juicesandsodas;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.init.ModBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.foliageplacer.BlobFoliagePlacer;
import net.minecraft.world.gen.treedecorator.BeehiveTreeDecorator;
import net.minecraft.world.gen.trunkplacer.StraightTrunkPlacer;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Base.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFeatures {

    public static final ConfiguredFeature<BaseTreeFeatureConfig, ?> LEMON = Feature.TREE.withConfiguration((new BaseTreeFeatureConfig.Builder(new SimpleBlockStateProvider(Blocks.OAK_LOG.getDefaultState()), new SimpleBlockStateProvider(ModBlocks.LEMON_LEAVES.getDefaultState()), new BlobFoliagePlacer(FeatureSpread.func_242252_a(2), FeatureSpread.func_242252_a(0), 3), new StraightTrunkPlacer(3, 1, 0), new TwoLayerFeature(1, 0, 1)).setIgnoreVines().setDecorators(ImmutableList.of(new BeehiveTreeDecorator(0.05F)))).build());


    public static ConfiguredFeature<?, ?> TREE_LEMON_CONFIG;

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        TREE_LEMON_CONFIG = Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, "tree_lemon",
                Feature.TREE.withConfiguration(LEMON.config));
    }

    @SubscribeEvent
    public void onBiomeLoading(final BiomeLoadingEvent biome) {
        if(biome.getCategory() == Biome.Category.NETHER || biome.getCategory() == Biome.Category.THEEND) return;

        biome.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION)
                .add(() ->  TREE_LEMON_CONFIG);
    }
}