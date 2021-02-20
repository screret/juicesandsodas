package io.screret.github.juicesandsodas.util;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;

public class BlenderRecipes {

    private static Map<ItemStack[], ItemStack> blendRecipes = Maps.newHashMap();

    public static void addBlenderRecipe(ItemStack[] input, ItemStack output) {
        blendRecipes.put(input, output);
    }

    @Nonnull
    public ItemStack getBlendResult(ItemStack stack) {
        for (Entry<ItemStack[], ItemStack> entry : blendRecipes.entrySet()) {
            if (this.compareItemStacks(stack, entry.getKey()[0])) {
                return entry.getValue();
            }
        }
        return ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack getBlendResult3(ItemStack stack1, ItemStack stack2, ItemStack stack3) {
        for (Entry<ItemStack[], ItemStack> entry : blendRecipes.entrySet()) {
            if (this.compareItemStacks(stack1, entry.getKey()[0])) {
                if(this.compareItemStacks(stack2, entry.getKey()[1])){
                    if(this.compareItemStacks(stack3, entry.getKey()[2])){
                        return entry.getValue();
                    }
                }
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean compareItemStacks(ItemStack stack1, ItemStack stack2) {
        //return stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata());
        return compareItemStacks(stack1, stack2, false);
    }


    private boolean compareItemStacks(ItemStack key, ItemStack entry, boolean keyStackBiggerThanEntry) {
        return entry.getItem() == key.getItem() && (!keyStackBiggerThanEntry || key.getCount() >= entry.getCount());
    }

}
