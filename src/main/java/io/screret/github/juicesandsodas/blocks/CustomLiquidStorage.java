package io.screret.github.juicesandsodas.blocks;


import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class CustomLiquidStorage extends FluidTank implements INBTSerializable<CompoundNBT> {

    public CustomLiquidStorage(int capacity, TileEntity tile) {
        super(capacity);
    }


    public void setFluid(int fluid) {
        this.fluid.setAmount(fluid);
    }

    public void addFluid(int amount) {
        this.fluid.setAmount(this.fluid.getAmount() + amount);
        if (this.fluid.getAmount() > this.getCapacity()) {
            this.fluid.setAmount(this.getFluidAmount());
        }

    }


    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("fluid", this.getCapacity());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setFluid(nbt.getInt("fluid"));
    }
}
