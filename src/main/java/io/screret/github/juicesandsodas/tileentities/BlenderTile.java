package io.screret.github.juicesandsodas.tileentities;


import io.screret.github.juicesandsodas.blocks.BlenderZoneContents;
import io.screret.github.juicesandsodas.containers.BlenderBlockContainer;
import io.screret.github.juicesandsodas.init.Registry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlenderTile extends TileEntity implements ITickableTileEntity, INamedContainerProvider {

    private boolean needsUpdate = true;
    private int updateTimer = 0;

    private static final String CONTENTS_INVENTORY_TAG = "contents";

    public static final int NUMBER_OF_SLOTS = 6;

    private BlenderZoneContents contents;

    public BlenderTile() {
        super(Registry.BLENDER_TILE.get());
        contents = BlenderZoneContents.createForTileEntity(NUMBER_OF_SLOTS, this::canPlayerAccessInventory, this::markDirty);
    }
    public boolean canPlayerAccessInventory(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) return false;
        final double X_CENTRE_OFFSET = 0.5;
        final double Y_CENTRE_OFFSET = 0.5;
        final double Z_CENTRE_OFFSET = 0.5;
        final double MAXIMUM_DISTANCE_SQ = 8.0 * 8.0;
        return player.getDistanceSq(pos.getX() + X_CENTRE_OFFSET, pos.getY() + Y_CENTRE_OFFSET, pos.getZ() + Z_CENTRE_OFFSET) < MAXIMUM_DISTANCE_SQ;
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
        super.read(blockState, parentNBTTagCompound); // The super call is required to save and load the tiles location
        parentNBTTagCompound.get(CONTENTS_INVENTORY_TAG);
        if (this.contents.getSizeInventory() != NUMBER_OF_SLOTS) {
            throw new IllegalArgumentException("Corrupted NBT: Number of inventory slots did not match expected.");
        }
    }

    @Nullable
    @Override
    public Container createMenu(int windowID, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return BlenderBlockContainer.createContainerServerSide(windowID, playerInventory, contents);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("gui.juicesandsodas.blendercontainer");
    }

    @Override
    public void tick() {

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
}
