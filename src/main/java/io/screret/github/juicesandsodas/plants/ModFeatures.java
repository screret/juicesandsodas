package io.screret.github.juicesandsodas.plants;

import com.google.common.collect.ImmutableList;
import io.screret.github.juicesandsodas.Base;
import io.screret.github.juicesandsodas.init.ModStuff;
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

@Mod.EventBusSubscriber(modid = Base.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModFeatures {

    public static ConfiguredFeature<BaseTreeFeatureConfig, ?> LEMON_TREE;


    public static ConfiguredFeature<?, ?> TREE_LEMON_CONFIG;

    @SubscribeEvent
    public void onBiomeLoading(final BiomeLoadingEvent biome) {
        if(biome.getCategory() == Biome.Category.NETHER || biome.getCategory() == Biome.Category.THEEND) return;

        biome.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION)
                .add(() ->  TREE_LEMON_CONFIG);
    }
}