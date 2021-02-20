package io.screret.github.juicesandsodas.creativeTabs;

import io.screret.github.juicesandsodas.init.Registration;
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
        return new ItemStack(Registration.KOOL_AID.get());
    }
}

