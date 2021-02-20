package io.screret.github.juicesandsodas;

import io.screret.github.juicesandsodas.init.Registration;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.IPlantable;

import java.util.function.Supplier;

public enum FruitType {
    MANDARIN(Blocks.OAK_LOG, Registration.MANDARIN_LEAVES.get(), () -> (SaplingBlock) Registration.MANDARIN_SAPLING.get(), Registration.MANDARIN.get()),
    LIME(Blocks.OAK_LOG, Registration.LIME_LEAVES.get(), () -> (SaplingBlock) Registration.LIME_SAPLING.get(), Registration.LIME.get()),
    ORANGE(Blocks.OAK_LOG, Registration.ORANGE_LEAVES.get(), () -> (SaplingBlock) Registration.ORANGE_SAPLING.get(), Registration.ORANGE.get()),
    LEMON(Blocks.OAK_LOG, Registration.LEMON_LEAVES.get(), () -> (SaplingBlock) Registration.LEMON_SAPLING.get(), Registration.LEMON.get()),
    GRAPEFRUIT(Blocks.OAK_LOG, Registration.GRAPEFRUIT_LEAVES.get(), () -> (SaplingBlock) Registration.GRAPEFRUIT_SAPLING.get(), Registration.GRAPEFRUIT.get()),
    APPLE(Blocks.OAK_LOG, Registration.APPLE_LEAVES.get(), () -> (SaplingBlock) Registration.APPLE_SAPLING.get(), Items.APPLE);

    public final Block log;
    public final LeavesBlock leaves;
    public final Supplier<SaplingBlock> sapling;
    public final Item fruit;

    private FruitType(Block log, Block leaves, Supplier<SaplingBlock> sapling, Item fruit) {
        this.log = log;
        this.leaves = (LeavesBlock) leaves;
        this.sapling = sapling;
        this.fruit = fruit;
    }

    public static FruitType create(String name, Block log, LeavesBlock leaves, Supplier<IPlantable> sapling, Item fruit) {
        throw new IllegalStateException("Enum not extended");
    }

    public static FruitType parse(String name) {
        try {
            return valueOf(name);
        } catch (Exception e) {
            return LEMON;
        }
    }
}
