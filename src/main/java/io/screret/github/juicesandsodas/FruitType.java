package io.screret.github.juicesandsodas;

import java.util.function.Supplier;

import io.screret.github.juicesandsodas.init.Registry;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.common.IPlantable;

public enum FruitType {
    MANDARIN(Blocks.OAK_LOG, Registry.MANDARIN_LEAVES.get(), () -> (SaplingBlock) Registry.MANDARIN_SAPLING.get(), Registry.MANDARIN.get()),
    LIME(Blocks.OAK_LOG, Registry.LIME_LEAVES.get(), () -> (SaplingBlock) Registry.LIME_SAPLING.get(), Registry.LIME.get()),
    CITRON(Blocks.OAK_LOG, Registry.CITRON_LEAVES.get(), () -> (SaplingBlock) Registry.CITRON_SAPLING.get(), Registry.CITRON.get()),
    POMELO(Blocks.OAK_LOG, Registry.POMELO_LEAVES.get(), () -> (SaplingBlock) Registry.POMELO_SAPLING.get(), Registry.POMELO.get()),
    ORANGE(Blocks.OAK_LOG, Registry.ORANGE_LEAVES.get(), () -> (SaplingBlock) Registry.ORANGE_SAPLING.get(), Registry.ORANGE.get()),
    LEMON(Blocks.OAK_LOG, Registry.LEMON_LEAVES.get(), () -> (SaplingBlock) Registry.LEMON_SAPLING.get(), Registry.LEMON.get()),
    GRAPEFRUIT(Blocks.OAK_LOG, Registry.GRAPEFRUIT_LEAVES.get(), () -> (SaplingBlock) Registry.GRAPEFRUIT_SAPLING.get(), Registry.GRAPEFRUIT.get()),
    APPLE(Blocks.OAK_LOG, Registry.APPLE_LEAVES.get(), () -> (SaplingBlock) Registry.APPLE_SAPLING.get(), Items.APPLE);

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
            return CITRON;
        }
    }
}
