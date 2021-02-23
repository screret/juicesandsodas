package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.util.BlenderRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlenderTile extends TileEntity implements IItemHandler, ITickableTileEntity {

    public ItemStackHandler inputSlot = new ItemStackHandler(3);
    public ItemStackHandler bottleSlot =  new ItemStackHandler(1);
    public ItemStackHandler outputSlot = new ItemStackHandler(3);


    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public static final int NUMBER_OF_SLOTS = 6;

    private static int BLEND_TIME = 60;


    /* FOLLOWING Code helps the copied code below. */

    public static int COOK_TIME = 2;
    public static int COOK_TIME_TOTAL = 3;
    public static int RECIPES_USED = 1;

    /* FOLLOWING Code is copied from "Shadows-of-Fire/FastFurnace" mod to enhance performance */

    public static final int INPUT = 0;
    public static final int OUTPUT = 5;

    public final IIntArray blenderData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                case 2:
                    return COOK_TIME;
                case 1:
                    return RECIPES_USED;
                case 3:
                    return COOK_TIME_TOTAL;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    BLEND_TIME = value;
                    break;
                case 1:
                    RECIPES_USED = value;
                    break;
                case 2:
                    COOK_TIME = value;
                    break;
                case 3:
                    COOK_TIME_TOTAL= value;
            }

        }

        public int size() {
            return 7;
        }
    };

    public static NonNullList<ItemStack> ITEMS = NonNullList.withSize(7, ItemStack.EMPTY);

    protected final IRecipeType<? extends BlenderRecipe> recipeType;


    protected IRecipe curRecipe;

    public BlenderTile( IRecipeType<? extends BlenderRecipe> recipeTypeIn) {
        super(Registration.BLENDER_TILE.get());

        this.recipeType = recipeTypeIn;
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        parentNBTTagCompound.putFloat("juicesandsodas:blendTime", BLEND_TIME);
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

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    @Override
    public void tick() {
        if (world.isRemote) return;
        if (this.isBlending()) {
            this.blenderData.set(BLEND_TIME, this.blenderData.get(BLEND_TIME) - 1);
        }
        IRecipe irecipe = getRecipe();
        boolean valid = this.canSmelt(irecipe);
        if (this.world != null && !this.world.isRemote) {
            if (this.isBlending() && !this.ITEMS.get(INPUT).isEmpty()) {
                if (!this.isBlending() && valid) {
                    this.blenderData.set(RECIPES_USED, this.blenderData.get(BLEND_TIME));
                }
            }

            if (this.isBlending() && valid) {
                this.blenderData.set(COOK_TIME, this.blenderData.get(COOK_TIME) + 1);
                if (this.blenderData.get(COOK_TIME) == this.blenderData.get(COOK_TIME_TOTAL)) {
                    this.blenderData.set(COOK_TIME, 0);
                    this.blenderData.set(COOK_TIME_TOTAL, this.getCookTime());
                    this.smeltItem(irecipe);
                }
            } else {
                this.blenderData.set(COOK_TIME, 0);
            }
        } else if (!this.isBlending() && this.blenderData.get(COOK_TIME) > 0) {
            this.blenderData.set(COOK_TIME, MathHelper.clamp(this.blenderData.get(COOK_TIME) - 2, 0, this.blenderData.get(COOK_TIME_TOTAL)));
        }
    }

    private void smeltItem(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = this.ITEMS.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = this.ITEMS.get(2);
            if (itemstack2.isEmpty()) {
                this.ITEMS.set(2, itemstack1.copy());
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.world != null && !this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            if (itemstack.getItem() == Blocks.WET_SPONGE.asItem() && !this.ITEMS.get(1).isEmpty() && this.ITEMS.get(1).getItem() == Items.BUCKET) {
                this.ITEMS.set(1, new ItemStack(Items.WATER_BUCKET));
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
        return this.blenderData.get(BLEND_TIME) > 0;
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot. For
     * guis use Slot.isItemValid
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (index == 4 || index == 5 || index == 6) {
            return false;
        } else if (index != 3) {
            return true;
        } else {
            ItemStack itemstack = this.ITEMS.get(3);
            return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
        }
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

    protected boolean canSmelt(@Nullable IRecipe<?> recipe) {
        if (!ITEMS.get(0).isEmpty() && !ITEMS.get(1).isEmpty() && !ITEMS.get(2).isEmpty() && recipe != null) {
            ItemStack recipeOutput = recipe.getRecipeOutput();
            if (!recipeOutput.isEmpty()) {
                ItemStack output = ITEMS.get(OUTPUT);
                if (output.isEmpty()) return true;
                else if (!output.isItemEqual(recipeOutput)) return false;
                else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
            }
        }
        return false;
    }

    @Override
    public int getSlots() {
        return 6;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return ITEMS.get(slot);
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
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 4 || slot == 5 || slot == 6) {
            return false;
        } else if (slot != 3) {
            return true;
        } else {
            ItemStack itemstack = this.ITEMS.get(3);
            return isFuel(stack) || stack.getItem() == Items.BUCKET && itemstack.getItem() != Items.BUCKET;
        }
    }

    protected IRecipe getRecipe() {
        ItemStack input1 = this.getStackInSlot(INPUT);
        ItemStack input2 = this.getStackInSlot(INPUT + 1);
        ItemStack input3 = this.getStackInSlot(INPUT + 2);
        if (input1.isEmpty() || input1 == ItemStack.EMPTY && input2.isEmpty() || input2 == ItemStack.EMPTY && input3.isEmpty() || input3 == ItemStack.EMPTY) {
            return null;
        }
        if (this.world != null && curRecipe != null && curRecipe.matches((IInventory) this.inputSlot, world)) {
            return curRecipe;
        } else {
            IRecipe rec = null;
            if (this.world != null) {
                rec = this.world.getRecipeManager().getRecipe(IRecipeType.register("blending"), (IInventory) this.inputSlot, this.world).orElse(null);
            }
            return curRecipe = rec;
        }
    }

    public void setRecipeUsed(@javax.annotation.Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    protected int getCookTime() {
        return this.world.getRecipeManager().getRecipe((IRecipeType<BlenderRecipe>)this.recipeType, (IInventory) this.inputSlot, this.world).map(BlenderRecipe::getCookTime).orElse(200);
    }

}
