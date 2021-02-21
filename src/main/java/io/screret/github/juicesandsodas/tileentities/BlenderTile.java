package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.init.Registration;
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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Random;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IBlenderRod.class
)
public class BlenderTile extends TileEntity implements IBlenderRod, ITickableTileEntity, INamedContainerProvider {

    public ItemStackHandler inputSlot = createInputHandler(3);
    public ItemStackHandler bottleSlot = createBottleHandler(1);
    public ItemStackHandler outputSlot = new ItemStackHandler();

    public ItemStackHandler outputSlotWrapper;

    public static final int NUMBER_OF_SLOTS = 6;

    private float blendTime = 60;
    private final BlenderRecipes recipes = new BlenderRecipes();
    /** The current angle of the lid (between 0 and 1) */
    protected float rodAngle;
    /** The angle of the lid last tick */
    protected float prevRodAngle;

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
        return new BlenderBlockContainer(windowID, playerInventory, new CombinedInvWrapper(inputSlot, bottleSlot, outputSlotWrapper), this);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.juicesandsodas.blendercontainer");
    }

    @Override
    public void tick() {
        if(world.isRemote) return;
        if(world.getBlockState(pos).getBlock() != this.getBlockState().getBlock()) return;
        if (blendTime > 0) {
            blendTime -= 1;
            if (inputSlot.getStackInSlot(0).isEmpty()) {
                blendTime = 0;
            } else if (blendTime <= 0) {
                blendItem();
                this.rodAngle += 0.1F;
                if (!outputSlot.getStackInSlot(0).isEmpty() && (outputSlot.getStackInSlot(0).getCount() >= 1 || inputSlot.getStackInSlot(0).isEmpty())) {
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

    private void blendItem() {

        ItemStack result = recipes.getBlendResult3(new ItemStack(inputSlot.getStackInSlot(0).getItem()),
                new ItemStack(inputSlot.getStackInSlot(1).getItem()),
                new ItemStack(inputSlot.getStackInSlot(2).getItem())).copy();

        outputSlot.insertItem(0, result, false);

        inputSlot.extractItem(0, 1, false);
        inputSlot.extractItem(1, 1, false);
        inputSlot.extractItem(2, 1, false);
        this.markDirty();
    }


    @OnlyIn(Dist.CLIENT)
    public float getRodAngle(float partialTicks) {
        return MathHelper.lerp(partialTicks, this.prevRodAngle, this.rodAngle);
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
                return true/*stack.getItem() == Registration.LIME.get() ||
                        stack.getItem() == Registration.LEMON.get() ||
                        stack.getItem() == Registration.CHERRY.get() ||
                        stack.getItem() == Registration.GRAPEFRUIT.get() ||
                        stack.getItem() == Registration.ORANGE.get() ||
                        stack.getItem() == Registration.MANDARIN.get() ||
                        stack.getItem() == Registration.LEMONADE.get() ||
                        stack.getItem() == Registration.KOOL_AID.get() ||
                        stack.getItem() == Registration.MAGIC_AID.get() ||
                        stack.getItem() == Registration.LIME_SODA.get() ||
                        stack.getItem() == Registration.GRAPE_JUICE.get()*/;
            }

            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                /*if (stack.getItem() != Registration.LIME.get() ||
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
                }*/
                return super.insertItem(slot, stack, simulate);
            }
        };
    }

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
                return stack.getItem() == Registration.EMPTY_JUICE_BOTTLE.get() ||
                        stack.getItem() == Registration.EMPTY_BOTTLE.get();
            }

            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (stack.getItem() == Registration.EMPTY_JUICE_BOTTLE.get() ||
                        stack.getItem() == Registration.EMPTY_BOTTLE.get()) {
                    return stack;
                }
                return super.insertItem(slot, stack, simulate);
            }
        };
    }
}
