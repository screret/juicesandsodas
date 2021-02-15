package io.screret.github.juicesandsodas.blocks;


import io.screret.github.juicesandsodas.init.ModStuff;
import net.minecraft.fluid.Fluid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class BlenderTile extends TileEntity implements IFluidHandler, ITickableTileEntity {

    public FluidTank tank = new FluidTank(10000);
    private boolean needsUpdate = true;
    private int updateTimer = 0;


    public BlenderTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public BlenderTile() {
        super(ModStuff.BLENDER_TILE.get());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        needsUpdate = true;
        return this.tank.fill(resource, action);
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return this.tank.drain(resource.getAmount(), action);
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        needsUpdate = true;
        return this.tank.drain(maxDrain, action);
    }

    public float getAdjustedVolume() {
        float amount = tank.getFluidAmount();
        float capacity = tank.getCapacity();
        float volume = (amount / capacity) * 0.8F;
        return volume;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 3;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public void tick() {

    }
}
