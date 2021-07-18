package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.blocks.BlenderBlock;
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
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
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

    RecipeWrapper recipeWrapper = new RecipeWrapper(inputSlot);

    private final Object2IntOpenHashMap<ResourceLocation> recipes = new Object2IntOpenHashMap<>();
    public CombinedInvWrapper combinedInvWrapper = new CombinedInvWrapper(inputSlot, bottleSlot, outputSlot);

    static Logger LOGGER = LogManager.getLogger();

    public static final int NUMBER_OF_SLOTS = 6;

    public int COOK_TIME = 1;
    public int COOK_TIME_TOTAL = 150;
    public int RECIPES_USED = 1;

    public final int INPUT = 0;
    public final int OUTPUT = 4;

    public final IIntArray blenderData = new IIntArray() {
        public int get(int index) {
            switch(index) {
                case 0:
                    return COOK_TIME;
                case 1:
                    return RECIPES_USED;
                case 2:
                    return COOK_TIME_TOTAL;
                default:
                    return 1;
            }
        }

        public void set(int index, int value) {
            switch(index) {
                case 0:
                    COOK_TIME = value;
                    break;
                case 1:
                    RECIPES_USED = value;
                    break;
                case 2:
                    COOK_TIME_TOTAL= value;
                    break;
            }

        }

        public int size() {
            return 3;
        }
    };

    protected ItemStack failedMatch = ItemStack.EMPTY;

    protected BlenderRecipe curRecipe;
    protected final IRecipeType<? extends BlenderRecipe> recipeType;

    public BlenderTile() {
        super(Registration.BLENDER_TILE.get());
        this.recipeType = BlenderRecipeSerializer.BLENDING;
    }

    @Override
    public CompoundNBT save(CompoundNBT parentNBTTagCompound) {
        super.save(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        parentNBTTagCompound.putInt("juicesandsodas:blendTime", COOK_TIME);
        parentNBTTagCompound.putInt("juicesandsodas:blendTimeTotal", COOK_TIME_TOTAL);
        parentNBTTagCompound.put("juicesandsodas:inputslot", inputSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:bottleslot", bottleSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:outputSlot", outputSlot.serializeNBT());
        combinedInvWrapper = new CombinedInvWrapper(inputSlot, bottleSlot, outputSlot);
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in write
    @Override
    public void load(BlockState blockState, CompoundNBT parentNBTTagCompound) {
        super.load(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        COOK_TIME =  parentNBTTagCompound.getInt("juicesandsodas:blendTime");
        COOK_TIME_TOTAL = parentNBTTagCompound.getInt("juicesandsodas:blendTimeTotal");
        inputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:inputslot"));
        bottleSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:bottleslot"));
        outputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:outputSlot"));
        combinedInvWrapper = new CombinedInvWrapper(inputSlot, bottleSlot, outputSlot);
        //LOGGER.debug(world.getRecipeManager().getRecipesForType(BlenderRecipeSerializer.BLENDING));
    }

    public static boolean isFuel(ItemStack stack) {
        return net.minecraftforge.common.ForgeHooks.getBurnTime(stack) > 0;
    }

    @Override
    public void tick() {
        if (!level.isClientSide) return;
        IRecipe<IInventory> irecipe = getRecipe();
        boolean valid = this.canSmelt(irecipe);
        if (this.level != null) {
            /*if (isBlending() || !inputSlot.getStackInSlot(0).isEmpty() && !items.get(0).isEmpty()) {
                if (!this.isBlending() && valid) {
                    this.blenderData.set(RECIPES_USED, this.blenderData.get(COOK_TIME));
                }
            }*/

            if (isBlending() || !inputSlot.getStackInSlot(0).isEmpty() && valid) {
                level.setBlockAndUpdate(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false).setValue(BlenderBlock.BLENDING, true));
                int cooktime = this.blenderData.get(0) + 1;
                this.blenderData.set(0, cooktime);
                if (this.blenderData.get(0) >= this.blenderData.get(2)) {
                    //LOGGER.debug(this.isBlending() + ", " + this.COOK_TIME + ", " + this.blenderData.get(0) + ", " + this.toString());
                    this.blenderData.set(0, 0);
                    this.blenderData.set(2, 150);
                    this.smeltItem(irecipe);
                }
            }
        }/* else if (!this.isBlending()) {
            this.blenderData.set(COOK_TIME, MathHelper.clamp(this.blenderData.get(COOK_TIME) - 2, 0, this.blenderData.get(COOK_TIME_TOTAL)));
        }*/
        this.setChanged();
    }

    private void smeltItem(@Nullable IRecipe<IInventory> recipe) {
        if (recipe != null && this.canSmelt(recipe)) {
            ItemStack input1 = inputSlot.getStackInSlot(0);
            ItemStack input2 = inputSlot.getStackInSlot(1);
            ItemStack input3 = inputSlot.getStackInSlot(2);
            ItemStack smelt = bottleSlot.getStackInSlot(0);
            ItemStack itemStack1 = recipe.assemble(recipeWrapper);
            if (smelt.isEmpty()) {
                return;
            } else {
                if(!outputSlot.getStackInSlot(0).isEmpty()){
                    if(!outputSlot.getStackInSlot(1).isEmpty()) {
                        if(!outputSlot.getStackInSlot(2).isEmpty()) {
                            return;
                        } else outputSlot.setStackInSlot(2, itemStack1.copy());
                    } else outputSlot.setStackInSlot(1, itemStack1.copy());
                } else outputSlot.setStackInSlot(0, itemStack1.copy());
            }

            if (this.level != null && this.level.isClientSide) {
                this.setRecipeUsed(recipe);
            }

            input1.shrink(1);
            input2.shrink(1);
            input3.shrink(1);
            smelt.shrink(1);
            level.(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false).setValue(BlenderBlock.BLENDING, false));
        }
    }

    public boolean isBlending() {
        return this.blenderData.get(0) > 0;
    }

    protected boolean canSmelt(IRecipe<?> recipe) {
        if (!inputSlot.getStackInSlot(0).isEmpty() && !inputSlot.getStackInSlot(1).isEmpty() && !inputSlot.getStackInSlot(2).isEmpty() && !bottleSlot.getStackInSlot(0).isEmpty()) {
            if(recipe != null){
                ItemStack recipeOutput = recipe.getResultItem();
                if (!recipeOutput.isEmpty()) {
                    ItemStack output = outputSlot.getStackInSlot(0);
                    if (output.isEmpty()) return true;
                    else if (!output.sameItem(recipeOutput)) return false;
                    else return output.getCount() + recipeOutput.getCount() <= output.getMaxStackSize();
                }
            }
        }
        return false;
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot == 4 || slot == 5 || slot == 6) {
            return false;
        } else if (slot != 3) {
            return true;
        } else {
            return stack.copy().getItem() == Registration.EMPTY_BOTTLE.get() || stack.copy().getItem() == Registration.EMPTY_JUICE_BOTTLE.get();
        }
    }

    protected BlenderRecipe getRecipe() {
        ItemStack input1 = inputSlot.getStackInSlot(0);
        ItemStack input2 = inputSlot.getStackInSlot(1);
        ItemStack input3 = inputSlot.getStackInSlot(2);

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

    /*public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > 64) {
            stack.setCount(64);
        }

        if (index == 0 && !flag) {
            COOK_TIME_TOTAL = COOK_TIME;
            COOK_TIME = 0;
            this.markDirty();
        }

    }*/

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
