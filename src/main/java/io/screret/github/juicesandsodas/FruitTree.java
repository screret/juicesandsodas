package io.screret.github.juicesandsodas;

import java.util.Random;
import java.util.function.Supplier;

import io.screret.github.juicesandsodas.init.Registration;
import net.minecraft.block.trees.Tree;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class FruitTree extends Tree {

    private final Supplier<FruitType> typeSupplier;

    public FruitTree(Supplier<FruitType> type) {
        this.typeSupplier = type;
    }

    @Override
    protected ConfiguredFeature<BaseTreeFeatureConfig, ?> getTreeFeature(Random random, boolean largeHive) {
        FruitType type = typeSupplier.get();
        return Registration.buildTreeFeature(type, false, null);
    }

}
