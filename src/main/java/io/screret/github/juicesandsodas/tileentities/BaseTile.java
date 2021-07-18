package io.screret.github.juicesandsodas.tileentities;


import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

import javax.annotation.Nonnull;

/**
 * Base TileEntity skeleton used by all TileEntity. It contains several standardized
 * implementations regarding networking.
 */
public abstract class BaseTile extends TileEntity {
    public boolean persistData = false;

    public BaseTile(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public final SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.worldPosition, -1, this.writePacketData(new CompoundNBT()));
    }

    @Override
    public final void onDataPacket(NetworkManager manager, SUpdateTileEntityPacket packet) {
        this.readPacketData(packet.getTag());
    }

    // Used for syncing data at the time when the chunk is loaded
    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return this.writePacketData(new CompoundNBT());
    }

    // Used for syncing data at the time when the chunk is loaded
    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.deserializeNBT(state, tag);
    }

    /**
     * Read data for server-client syncing.
     *
     * @param data
     *            the data source
     */
    protected abstract void readPacketData(CompoundNBT data);

    /**
     * Write data for server-client syncing. ONLY write the necessary data!
     *
     * @param data
     *            the data sink
     * @return the parameter, or delegate to super method
     */
    @Nonnull
    protected abstract CompoundNBT writePacketData(CompoundNBT data);

    protected void refresh() {
        if (this.hasLevel() && level.isClientSide) {
            BlockState state = level.getBlockState(getBlockPos());
            level.markAndNotifyBlock(getBlockPos(), level.getChunkAt(getBlockPos()), state, state, 11, 512 /* TODO whats this? */);
        }
    }

}