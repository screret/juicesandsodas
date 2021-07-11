package io.screret.github.juicesandsodas.containers;

import io.screret.github.juicesandsodas.init.Registration;
import io.screret.github.juicesandsodas.tileentities.BlenderTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class BlenderBlockContainer extends Container {

    private final IItemHandler playerInventory;
    private final IItemHandler inventory;

    private final IIntArray blenderData;

    private static final int HOTBAR_SLOT_COUNT = 9;
	private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
	private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
	private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
	private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;

	private static final int VANILLA_FIRST_SLOT_INDEX = 0;
	private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;
	private static final int TE_INVENTORY_SLOT_COUNT = BlenderTile.NUMBER_OF_SLOTS;  // must match TileEntityInventoryBasic.NUMBER_OF_SLOTS

    public static final int TITLE_INVENTORY_YPOS = 20;  // the ContainerScreenBasic needs to know these so it can tell where to draw the Titles
    public static final int PLAYER_INVENTORY_YPOS = 84;
    public static final int PLAYER_INVENTORY_XPOS = 8;

    public static final int INV_SIZE = 6;

    private static final Logger LOGGER = LogManager.getLogger();


    public BlenderBlockContainer(int windowID, PlayerInventory playerInventory, IItemHandler inven, BlenderTile tileEntity) {
        super(Registration.BLENDER_CONT.get(), windowID);
        this.playerInventory = new InvWrapper(playerInventory);
        blenderData = tileEntity.blenderData;
        this.inventory = inven;

        final int SLOT_X_SPACING = 18;
        final int SLOT_Y_SPACING = 18;

        layoutPlayerInventorySlots(8, 84);
        if (tileEntity != null) {
            final int INPUT_SLOTS_XPOS = 24;
            final int INPUT_SLOTS_YPOS = 16;
            final int BOTTLE_SLOT_XPOS = 59;
            final int BOTTLE_SLOT_YPOS = 34;
            this.addSlot(slotHandler(inven, 0, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS + SLOT_Y_SPACING * 0));
            this.addSlot(slotHandler(inven, 1, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS + SLOT_Y_SPACING * 1));
            this.addSlot(slotHandler(inven, 2, INPUT_SLOTS_XPOS, INPUT_SLOTS_YPOS + SLOT_Y_SPACING * 2));
            this.addSlot(slotHandler(inven, 3, BOTTLE_SLOT_XPOS, BOTTLE_SLOT_YPOS));

            final int OUTPUT_SLOTS_XPOS = 113;
            final int OUTPUT_SLOTS_YPOS = 34;
            this.addSlot(slotHandler(inven, 4, OUTPUT_SLOTS_XPOS + SLOT_X_SPACING * 0, OUTPUT_SLOTS_YPOS));
            this.addSlot(slotHandler(inven, 5, OUTPUT_SLOTS_XPOS + SLOT_X_SPACING * 1, OUTPUT_SLOTS_YPOS));
            this.addSlot(slotHandler(inven, 6, OUTPUT_SLOTS_XPOS + SLOT_X_SPACING * 2, OUTPUT_SLOTS_YPOS));

        } else {
            throw new IllegalStateException("TileEntity is null");
        }
    }


    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack stack = slot.getStack();
            itemstack = stack.copy();
            if (index == 0) {
                if (!this.mergeItemStack(stack, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onSlotChange(stack, itemstack);
            }

            if (stack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (stack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, stack);
        }

        return itemstack;
    }

    @OnlyIn(Dist.CLIENT)
    public int getCookProgressionScaled() {
        int i = this.blenderData.get(0);
        int j = 150;
        //int j = this.blenderData.get(2);
        return j != 0 && i != 0 ? i * 24 / j : 0;
    }


    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        Predicate<PlayerEntity> canPlayerAccessInventoryLambda = x-> true;
        return canPlayerAccessInventoryLambda.test(playerIn);
    }


    private int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    private int addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
        return index;
    }

    private void layoutPlayerInventorySlots(int leftCol, int topRow) {
        // Player inventory
        addSlotBox(playerInventory, 9, leftCol, topRow, 9, 18, 3, 18);

        // Hotbar
        topRow += 58;
        addSlotRange(playerInventory, 0, leftCol, topRow, 9, 18);
    }

    public SlotItemHandler slotHandler(IItemHandler handler, int index, int xPosition, int yPosition){
        return new SlotItemHandler(handler, index, xPosition, yPosition){
            @Override
            public boolean isItemValid(@Nullable ItemStack stack) {
                if (index == 4 || index == 5 || index == 6) {
                    return false;
                } else if (index != 3) {
                    return true;
                } else {
                    return stack.copy().getItem() == Registration.EMPTY_BOTTLE.get() || stack.copy().getItem() == Registration.EMPTY_JUICE_BOTTLE.get();
                }
            }
        };
    }
}
