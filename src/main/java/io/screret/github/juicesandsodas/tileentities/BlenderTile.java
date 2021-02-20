package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.properties.block.BlockProperties;
import io.screret.github.juicesandsodas.properties.block.blender.BlenderRodOrientation;
import io.screret.github.juicesandsodas.util.BlenderRecipes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

public class BlenderTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    public ItemStackHandler inputSlot = createInputHandler(3);
    public ItemStackHandler outputSlot = createOutputHandler(3);

    public ItemStackHandler outputSlotWrapper;


    public static final int NUMBER_OF_SLOTS = 6;

    private float blendTime = 60;
    private final BlenderRecipes recipes = new BlenderRecipes();

    public BlenderTile() {
        super(Registration.BLENDER_TILE.get());

        outputSlotWrapper = new OutputItemStackHandler(outputSlot);
    }

    @Override
    public CompoundNBT write(CompoundNBT parentNBTTagCompound)
    {
        super.write(parentNBTTagCompound); // The super call is required to save and load the tileEntity's location
        parentNBTTagCompound.putFloat("juicesandsodas:blendTime", blendTime);
        parentNBTTagCompound.put("juicesandsodas:inputslot", inputSlot.serializeNBT());
        parentNBTTagCompound.put("juicesandsodas:outputSlot", outputSlot.serializeNBT());
        return parentNBTTagCompound;
    }

    // This is where you load the data that you saved in write
    @Override
    public void read(BlockState blockState, CompoundNBT parentNBTTagCompound)
    {
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        parentNBTTagCompound.get("juicesandsodas:blendTime");
        inputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:inputslot"));
        outputSlot.deserializeNBT(parentNBTTagCompound.getCompound("juicesandsodas:outputSlot"));
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new BlenderBlockContainer(windowID, playerInventory, new CombinedInvWrapper(inputSlot, outputSlotWrapper), this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.juicesandsodas.blendercontainer");
    }

    @Override
    public void tick() {
        if(world.isRemote) return;
        if(world.getBlockState(pos).getBlock() != this.getBlockState().getBlock()) return;
        BlenderRodOrientation rodOrientation = world.getBlockState(pos).get(BlockProperties.ROD_ORIENTATION);
        if(rodOrientation == BlenderRodOrientation.CENTER) {
            if (blendTime > 0) {
                blendTime -= 1;
                if (inputSlot.getStackInSlot(0).isEmpty()) {
                    blendTime = 0;
                } else if (blendTime <= 0) {
                    blendItem(rodOrientation);

                    if (!outputSlot.getStackInSlot(0).isEmpty() && (outputSlot.getStackInSlot(0).getCount() >= 8 || inputSlot.getStackInSlot(0).isEmpty())) {
                        if (!world.isRemote) {
                            BlockState s = world.getBlockState(pos.down());
                            if (s.getBlock().isAir(s, world, pos)) {
                                Random rand = world.rand;
                                float rx = rand.nextFloat() * 0.6F + 0.2F;
                                float ry = rand.nextFloat() * 0.2F + 0.6F - 1;
                                float rz = rand.nextFloat() * 0.6F + 0.2F;
                                ItemEntity itemEntity = new ItemEntity(world,
                                        pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz,
                                        outputSlot.extractItem(0, 64, false));
                                world.addEntity(itemEntity);
                                itemEntity.setMotion(0, -0.2F, 0);
                            }
                        }
                    }
                }
                this.markDirty();
            }
        }
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return null;
    }

    @Override
    public void requestModelDataUpdate() {

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

    private ItemStackHandler createInputHandler(int size) {
        return new ItemStackHandler(size) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            /*@Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return stack.getItem() == Registration.LIME.get() ||
                        stack.getItem() == Registration.LEMON.get() ||
                        stack.getItem() == Registration.CHERRY.get() ||
                        stack.getItem() == Registration.GRAPEFRUIT.get() ||
                        stack.getItem() == Registration.ORANGE.get() ||
                        stack.getItem() == Registration.MANDARIN.get() ||
                        stack.getItem() == Registration.LEMONADE.get() ||
                        stack.getItem() == Registration.KOOL_AID.get() ||
                        stack.getItem() == Registration.MAGIC_AID.get() ||
                        stack.getItem() == Registration.LIME_SODA.get() ||
                        stack.getItem() == Registration.GRAPE_JUICE.get();
            }*/

            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() != Registration.LIME.get() ||
                        stack.getItem() != Registration.CHERRY.get() ||
                        stack.getItem() != Registration.GRAPEFRUIT.get() ||
                        stack.getItem() != Registration.ORANGE.get() ||
                        stack.getItem() != Registration.MANDARIN.get() ||
                        stack.getItem() != Registration.LEMONADE.get() ||
                        stack.getItem() != Registration.KOOL_AID.get() ||
                        stack.getItem() != Registration.MAGIC_AID.get() ||
                        stack.getItem() == Registration.LIME_SODA.get() ||
                        stack.getItem() == Registration.GRAPE_JUICE.get()) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    private ItemStackHandler createOutputHandler(int size) {
        return new ItemStackHandler(size) {

            @Override
            protected void onContentsChanged(int slot) {
                // To make sure the TE persists when the chunk is saved later we need to
                // mark it dirty every time the item handler changes
                markDirty();
            }

            @Override
            public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
                return false;
            }
        };
    }

    private void blendItem(BlenderRodOrientation millpos) {

        ItemStack result = recipes.getBlendResult(inputSlot.getStackInSlot(0)).copy();

        outputSlot.insertItem(0, result, false);

        inputSlot.extractItem(0, 1, false);
        inputSlot.extractItem(1, 1, false);
        inputSlot.extractItem(2, 1, false);
        this.markDirty();
    }

    /**
     * ItemStackHandler wrapper that allows for all the usual inventory item manipulation except
     * that when exposed externally (GUI, hopper, etc.), items may not be inserted, only extracted.
     * @author Draco18s
     *
     */
    public class OutputItemStackHandler extends ItemStackHandler {
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
}
