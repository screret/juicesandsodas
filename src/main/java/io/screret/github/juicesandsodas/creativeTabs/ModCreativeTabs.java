package io.screret.github.juicesandsodas.creativeTabs;

import io.screret.github.juicesandsodas.init.ModStuff;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs extends ItemGroup
{
    public ModCreativeTabs()
    {
        super("juicesandsodas");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(ModStuff.KOOL_AID.get());
    }
}

