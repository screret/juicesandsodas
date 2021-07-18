package io.screret.github.juicesandsodas.tileentities;

import io.screret.github.juicesandsodas.FruitType;
import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.util.NBTHelper;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;

public class FruitTreeTile extends BaseTile {

    public FruitType type = FruitType.LEMON;
    private int deathRate = 0;
    private ItemEntity onlyItem;

    public FruitTreeTile() {
        super(Registration.FRUIT_TREE.get());
    }

    public FruitTreeTile(FruitType type) {
        this();
        this.type = type;
    }

    public int updateDeathRate() {
        return ++deathRate;
    }

    @Override
    protected void readPacketData(CompoundNBT data) {}

    @Override
    protected CompoundNBT writePacketData(CompoundNBT data) {
        return data;
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        NBTHelper helper = NBTHelper.of(compound);
        String id = helper.getString("type");
        if (id != null) {
            type = FruitType.parse(id);
            if (type == null) {
                type = FruitType.LEMON;
            }
        } else {
            FruitType[] types = FruitType.values();
            type = types[MathHelper.clamp(helper.getInt("type"), 0, types.length)];
        }
        deathRate = helper.getInt("death");
        super.load(state, compound);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.putString("type", type.name());
        compound.putInt("death", deathRate);
        return compound;
    }

    public boolean canDrop() {
        return onlyItem == null || !onlyItem.isAlive();
    }

    public void setOnlyItem(ItemEntity itementity) {
        onlyItem = itementity;
    }
}