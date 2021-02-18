package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.init.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlenderTile extends TileEntity implements IItemHandler, ITickableTileEntity, INamedContainerProvider {

    public FluidTank tank = new FluidTank(3);
    private boolean needsUpdate = true;
    private int updateTimer = 0;

    private static final String CONTENTS_INVENTORY_TAG = "contents";

    private final ItemStackHandler inventory = new ItemStackHandler();

    public static final int NUMBER_OF_SLOTS = 6;

    public BlenderTile() {
        super(Registry.BLENDER_TILE.get());
    }

    @Override
    public void tick() {

    }

    @Override
    public int getSlots() {
        return 6;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        CompoundNBT inventoryNBT = new CompoundNBT();
        parentNBTTagCompound.put(CONTENTS_INVENTORY_TAG, inventoryNBT);
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in write
    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        int windowId = BlenderBlockContainer.WINDOWID;
        IInventory inventory = BlenderBlockContainer.INV;
        BlenderBlockContainer container = new BlenderBlockContainer(windowId, Minecraft.getInstance().player.inventory, inventory);
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        CompoundNBT inventoryNBT = parentNBTTagCompound.getCompound(CONTENTS_INVENTORY_TAG);
        inventoryNBT.get(CONTENTS_INVENTORY_TAG);
        if (container.getSizeInventory() != NUMBER_OF_SLOTS) {
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
        }
    }

    @Nullable
    @Override
    public net.minecraft.inventory.container.Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return BlenderBlockContainer.createContainerServerSide(windowID, playerInventory, (IInventory) new ItemStackHandler(BlenderTile.NUMBER_OF_SLOTS));
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.juicesandsodas.blendercontainer");
    }
}
