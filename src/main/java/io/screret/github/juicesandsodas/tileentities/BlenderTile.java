package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.crafting.BlenderRecipeSerializer;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.util.BlenderRecipe;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

public class BlenderTile extends TileEntity implements ITickableTileEntity {

    public ItemStackHandler inputSlot = customHandler(3);
    public ItemStackHandler bottleSlot =  customHandler(1);
    public ItemStackHandler outputSlot = customHandler(3);


    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();

    public static final int NUMBER_OF_SLOTS = 6;

    private static int BLEND_TIME = 150;


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


    protected IRecipe<IInventory> curRecipe;

    public BlenderTile(IRecipeType<BlenderRecipe> recipeTypeIn) {
        super(Registration.BLENDER_TILE.get());

        BlenderRecipeSerializer.BLENDING = recipeTypeIn;
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
        IRecipe<IInventory> irecipe = getRecipe();
        boolean valid = this.canSmelt(irecipe);
        if (this.world != null && !this.world.isRemote) {
            if (this.isBlending() && !ITEMS.get(INPUT).isEmpty()) {
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
            ItemStack itemstack = ITEMS.get(0);
            ItemStack itemstack1 = recipe.getRecipeOutput();
            ItemStack itemstack2 = ITEMS.get(3);
            if (itemstack2.isEmpty()) {
                ITEMS.set(3, itemstack1.copy());
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

    public static boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 4 || slot == 5 || slot == 6) {
            return false;
        } else if (slot != 3) {
            return true;
        } else {
            return stack.copy().getItem() == Registration.EMPTY_BOTTLE.get() || stack.copy().getItem() == Registration.EMPTY_JUICE_BOTTLE.get();
        }
    }

    protected IRecipe<IInventory> getRecipe() {
        ItemStack input1 = inputSlot.getStackInSlot(INPUT);
        ItemStack input2 = inputSlot.getStackInSlot(INPUT + 1);
        ItemStack input3 = inputSlot.getStackInSlot(INPUT + 2);
        RecipeWrapper recipeWrapper = new RecipeWrapper(inputSlot);
        if ((input1.isEmpty() || input1 == ItemStack.EMPTY) && (input2.isEmpty() || input2 == ItemStack.EMPTY) && (input3.isEmpty() || input3 == ItemStack.EMPTY)) {
            return null;
        }
        if (this.world != null && curRecipe != null && curRecipe.matches(recipeWrapper, world)) {
            return curRecipe;
        } else {
            IRecipe<IInventory> rec = null;
            if (this.world != null) {

                rec = this.world.getRecipeManager().getRecipe(BlenderRecipeSerializer.BLENDING, recipeWrapper, this.world).orElse(null);
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
