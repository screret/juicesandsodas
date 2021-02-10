package io.screret.github.juicesandsodas.util;

import io.screret.github.juicesandsodas.Base;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class RegistryUtil {

    public static Item setItemName( final Item item, final String name) {
        item.setRegistryName(Base.MODID, name);
        return item;
    }

    public static Block setBlockName(final Block block, final String name) {
        block.setRegistryName(Base.MODID, name);
        return block;
    }

}