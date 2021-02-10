package io.screret.github.juicesandsodas.creativeTabs;

import io.screret.github.juicesandsodas.init.ModItems;
import net.minecraft.client.gui.screen.inventory.CreativeScreen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs extends ItemGroup
{
    public ModCreativeTabs()
    {
        super("Juices & Sodas");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModItems.KOOL_AID);
    }
}

