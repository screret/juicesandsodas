package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.crafting.BlenderRecipeSerializer;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.util.BlenderRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlenderTile extends TileEntity implements ITickableTileEntity {

    public ItemStackHandler inputSlot = customHandler(3);
    public ItemStackHandler bottleSlot =  customHandler(1);
    public ItemStackHandler outputSlot = customHandler(3);

    public NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY);
    public CombinedInvWrapper combinedInvWrapper = new CombinedInvWrapper(inputSlot, bottleSlot, outputSlot);

    static Logger LOGGER = LogManager.getLogger();

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public static final int NUMBER_OF_SLOTS = 6;

    private static int BLEND_TIME = 150;


    /* FOLLOWING Code helps the copied code below. */

    public static int COOK_TIME = 2;
    public static int COOK_TIME_TOTAL = 3;
    public static int RECIPES_USED = 1;

    /* FOLLOWING Code is copied from "Shadows-of-Fire/FastFurnace" mod to enhance performance */

    public static final int INPUT = 0;
    public static final int OUTPUT = 4;

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

    //public static NonNullList<ItemStack> ITEMS = NonNullList.withSize(7, ItemStack.EMPTY);

    protected ItemStack failedMatch;

    protected BlenderRecipe curRecipe;

    public BlenderTile() {
        super(Registration.BLENDER_TILE.get());
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound) {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        parentNBTTagCompound.putInt("juicesandsodas:blendTime", BLEND_TIME);
        parentNBTTagCompound.putInt("juicesandsodas:cookTime", COOK_TIME);
        parentNBTTagCompound.putInt("juicesandsodas:blendTimeTotal", COOK_TIME_TOTAL);
        parentNBTTagCompound.put("juicesandsodas:inputslot", inputSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:bottleslot", bottleSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:outputSlot", outputSlot.serializeNBT());
        ItemStackHelper.saveAllItems(parentNBTTagCompound, this.items);
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in write
    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        COOK_TIME =  parentNBTTagCompound.getInt("juicesandsodas:cookTime");
        BLEND_TIME =  parentNBTTagCompound.getInt("juicesandsodas:blendTime");
        COOK_TIME_TOTAL = parentNBTTagCompound.getInt("juicesandsodas:blendTimeTotal");
        inputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:inputslot"));
        bottleSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:bottleslot"));
        outputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:outputSlot"));
        ItemStackHelper.loadAllItems(parentNBTTagCompound, items);
        for (int i = 0; i < items.size(); i++){
            setInventorySlotContents(i, combinedInvWrapper.getStackInSlot(i));
        }
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
        IRecipe<?> irecipe = getRecipe();
        boolean valid = this.canSmelt(irecipe);
        if (this.world != null && !this.world.isRemote) {
            if (this.isBlending() && !items.get(0).isEmpty()) {
                if (!this.isBlending() && valid) {
                    this.blenderData.set(RECIPES_USED, this.blenderData.get(BLEND_TIME));
                }
            }

            if (this.isBlending() && valid) {
                this.blenderData.set(COOK_TIME, this.blenderData.get(COOK_TIME) + 1);
                if (this.blenderData.get(COOK_TIME) == this.blenderData.get(COOK_TIME_TOTAL)) {
                    this.blenderData.set(COOK_TIME, 0);
                    this.blenderData.set(COOK_TIME_TOTAL, 150);
                    this.smeltItem(irecipe);
                }
            } else {
                this.blenderData.set(COOK_TIME, 0);
            }
        } else if (!this.isBlending() && this.blenderData.get(COOK_TIME) > 0) {
            this.blenderData.set(COOK_TIME, MathHelper.clamp(this.blenderData.get(COOK_TIME) - 2, 0, this.blenderData.get(COOK_TIME_TOTAL)));
        }
        this.markDirty();
    }

    private void smeltItem(@Nullable IRecipe<?> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack itemstack = inputSlot.getStackInSlot(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = bottleSlot.getStackInSlot(0);
            if (itemstack2.isEmpty()) {
                return;
            } else if (itemstack2.getItem() == itemstack1.getItem()) {
                itemstack2.grow(itemstack1.getCount());
            }

            if (this.world != null && !this.world.isRemote) {
                this.setRecipeUsed(recipe);
            }

            itemstack.shrink(1);
        }
    }

    private boolean isBlending() {
        return this.blenderData.get(BLEND_TIME) > 0;
    }

    protected boolean canSmelt(IRecipe<?> recipe) {
        if (!items.get(0).isEmpty() && !items.get(1).isEmpty() && !items.get(2).isEmpty()) {
            if(recipe != null){
                ItemStack recipeOutput = recipe.getRecipeOutput();
                if (!recipeOutput.isEmpty()) {
                    ItemStack output = outputSlot.getStackInSlot(0);
                    if (output.isEmpty()) return true;
                    else if (!output.isItemEqual(recipeOutput)) return false;
                    else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    public static boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 4 || slot == 5 || slot == 6) {
            return false;
        } else if (slot != 3) {
            return true;
        } else {
            return stack.copy().getItem() == Registration.EMPTY_BOTTLE.get() || stack.copy().getItem() == Registration.EMPTY_JUICE_BOTTLE.get();
        }
    }

    protected BlenderRecipe getRecipe() {
        ItemStack input1 = inputSlot.getStackInSlot(INPUT);
        ItemStack input2 = inputSlot.getStackInSlot(INPUT + 1);
        ItemStack input3 = inputSlot.getStackInSlot(INPUT + 2);
        RecipeWrapper recipeWrapper = new RecipeWrapper(inputSlot);
        if (input1.isEmpty() && input2.isEmpty() && input3.isEmpty()) {
            return null;
        }
        if (this.world != null && curRecipe != null && curRecipe.matches(recipeWrapper, world)) {
            return curRecipe;
        } else {
            BlenderRecipe rec;
            if (this.world != null) {
                rec = this.world.getRecipeManager().getRecipe(BlenderRecipeSerializer.BLENDING, recipeWrapper, this.world).orElse(null);
                if (rec == null) failedMatch = input1;
                else failedMatch = ItemStack.EMPTY;
                return curRecipe = rec;
            }
        }
        return null;
    }

    public void setRecipeUsed(@Nullable IRecipe<?> recipe) {
        if (recipe != null) {
            ResourceLocation resourcelocation = recipe.getId();
            this.recipes.addTo(resourcelocation, 1);
        }
    }

    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > 64) {
            stack.setCount(64);
        }

        if (index == 0 && !flag) {
            this.COOK_TIME_TOTAL = this.COOK_TIME;
            this.COOK_TIME = 0;
            this.markDirty();
        }

    }

    public ItemStackHandler customHandler(int size){
        return new ItemStackHandler(size) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                if (slot == 4 || slot == 5 || slot == 6) {
                    return false;
                } else if (slot != 3) {
                    return true;
                } else {
                    return isFuel(stack) && (stack.copy().getItem() == Registration.EMPTY_BOTTLE.get() || stack.copy().getItem() == Registration.EMPTY_JUICE_BOTTLE.get());
                }
            }
        };
    }
}
