package io.screret.github.juicesandsodas.blocks;


import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.EnergyStorage;

public class CustomLiquidStorage extends FluidTank implements INBTSerializable<CompoundNBT> {

    public CustomLiquidStorage(int capacity, int maxTransfer, TileEntity tile) {
        super(capacity, maxTransfer, tile);
    }





    public void setFluid(int fluid) {
        this.fluid = fluid;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
        if (this.energy > getMaxEnergyStored()) {
            this.energy = getEnergyStored();
        }

    }


    
    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putInt("fluid", getFluidStored());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        setFluid(nbt.getInt("energy"));
    }
}
