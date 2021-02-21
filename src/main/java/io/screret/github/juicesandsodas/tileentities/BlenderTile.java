package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.FurnaceTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlenderTile extends FurnaceTileEntity implements ITickableTileEntity, INamedContainerProvider {

    public ItemStackHandler inputSlot = createInputHandler(3);
    public ItemStackHandler bottleSlot = createBottleHandler(1);
    public ItemStackHandler outputSlot = new ItemStackHandler();

    public ItemStackHandler outputSlotWrapper;

    public static final int NUMBER_OF_SLOTS = 6;

    private int blendTime = 60;


    /* FOLLOWING Code helps the copied code below. */

    public static final int COOK_TIME = 2;
    public static final int COOK_TIME_TOTAL = 3;
    public static final int RECIPES_USED = 1;

    /* FOLLOWING Code is copied from "Shadows-of-Fire/FastFurnace" mod to enhance performance */

    public static final int INPUT = 0;
    public static final int OUTPUT = 2;


    protected IRecipe<BlenderTile> curRecipe;

    public BlenderTile() {
        super();

        outputSlotWrapper = new OutputItemStackHandler(outputSlot);
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        parentNBTTagCompound.putFloat("juicesandsodas:blendTime", blendTime);
        parentNBTTagCompound.put("juicesandsodas:inputslot", inputSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:bottleslot", bottleSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:outputSlot", outputSlot.serializeNBT());
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in write
    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        parentNBTTagCompound.get("juicesandsodas:blendTime");
        inputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:inputslot"));
        bottleSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:bottleslot"));
        outputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:outputSlot"));
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlenderBlockContainer(windowID, playerInventory, new CombinedInvWrapper(inputSlot, bottleSlot, outputSlotWrapper), this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.juicesandsodas.blendercontainer");
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        if (this.isBlending()) {
            this.furnaceData.set(blendTime, this.furnaceData.get(blendTime) - 1); //changed because of private variable
        }
        IRecipe<BlenderTile> irecipe = getRecipe();
        boolean valid = this.canSmelt(irecipe);
        if (this.world != null && !this.world.isRemote) {
            if (this.isBlending() && !this.items.get(INPUT).isEmpty()) {
                if (!this.isBlending() && valid) {
                    this.furnaceData.set(RECIPES_USED, this.furnaceData.get(blendTime)); //changed because of private variable
                }
            }

            if (this.isBlending() && valid) {
                this.furnaceData.set(COOK_TIME, this.furnaceData.get(COOK_TIME) + 1); //changed because of private variable
                if (this.furnaceData.get(COOK_TIME) == this.furnaceData.get(COOK_TIME_TOTAL)) { //changed because of private variable
                    this.furnaceData.set(COOK_TIME, 0); //changed because of private variable
                    this.furnaceData.set(COOK_TIME_TOTAL, this.getCookTime()); //changed because of private variable
                    this.smeltItem(irecipe);
                }
            } else {
                this.furnaceData.set(COOK_TIME, 0); //changed because of private variable
            }
        } else if (!this.isBlending() && this.furnaceData.get(COOK_TIME) > 0) { //changed because of private variable
            this.furnaceData.set(COOK_TIME, MathHelper.clamp(this.furnaceData.get(COOK_TIME) - 2, 0, this.furnaceData.get(COOK_TIME_TOTAL))); //changed because of private variable
        }
    }

    private void smeltItem(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.items.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.items.get(2);
            if (itemstack2.isEmpty()) {
                this.items.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.world != null && !this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.items.get(1).isEmpty() && this.items.get(1).getItem() == Items.BUCKET) {
                this.items.set(1, new ItemStack(Items.WATER_BUCKET));
            }

            itemstack.shrink(1);
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getPos());
    }

    @Override
    public void requestModelDataUpdate() {

    }

    private boolean isBlending() {
        return this.furnaceData.get(blendTime) > 0; //changed because of private variable
    }


    @NotNull
    @Override
    public IModelData getModelData() {
        return new IModelData() {
            @Override
            public boolean hasProperty(ModelProperty<?> prop) {
                return false;
            }

            @Nullable
            @Override
            public <T> T getData(ModelProperty<T> prop) {
                return null;
            }

            @Nullable
            @Override
            public <T> T setData(ModelProperty<T> prop, T data) {
                return null;
            }
        };
    }

    @Override
    protected boolean canSmelt(@Nullable IRecipe<?> recipe) {
        if (!this.items.get(0).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = this.items.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.isItemEqual(recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    private ItemStackHandler createInputHandler(int size) {
        return new ItemStackHandler(size) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }
        };
    }

    public static class OutputItemStackHandler extends ItemStackHandler {
        private final ItemStackHandler internalSlot;

        public OutputItemStackHandler(ItemStackHandler hidden) {
            super();
            internalSlot = hidden;
        }

        @Override
        public void setSize(int size) {
            stacks = NonNullList.withSize(size, ItemStack.EMPTY);
        }

        @Override
        public void setStackInSlot(int slot, ItemStack stack) {
            internalSlot.setStackInSlot(slot, stack);
        }

        @Override
        public int getSlots() {
            return internalSlot.getSlots();
        }

        @Override
        public ItemStack getStackInSlot(int slot) {
            return internalSlot.getStackInSlot(slot);
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            return stack;
        }

        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return internalSlot.extractItem(slot, amount, simulate);
        }
    }

    private ItemStackHandler createBottleHandler(int size) {
        return new ItemStackHandler(size) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return super.isItemValid(slot, stack);
            }
        };
    }

    protected IRecipe<BlenderTile> getRecipe() {
        ItemStack input = this.getStackInSlot(INPUT);
        if (input.isEmpty() || input == ItemStack.EMPTY) {
            return null;
        }
        if (this.world != null && curRecipe != null && curRecipe.matches(this, world)) {
            return curRecipe;
        } else {
            IRecipe<BlenderTile> rec = null;
            if (this.world != null) {
                rec = this.world.getRecipeManager().getRecipe(IRecipeType.register("blending"), this, this.world).orElse(null);
            }
            return curRecipe = rec;
        }
    }
}
