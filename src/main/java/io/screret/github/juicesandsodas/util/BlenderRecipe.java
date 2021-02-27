package io.screret.github.juicesandsodas.util;

import io.screret.github.juicesandsodas.crafting.BlenderRecipeSerializer;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/**
 * Inputs: ItemStack (item) Output: ItemStack (transformed)
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlenderRecipe implements Predicate<@NotNull ItemStack>, IRecipe<IInventory> {

    public static Ingredient INGREDIENT;
    public static ItemStack OUTPUT;

    public static ResourceLocation id;

    public BlenderRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        super();
        INGREDIENT = input;
        OUTPUT = output.copy();
        BlenderRecipe.id = id;
    }

    @Override
    public boolean test(ItemStack input) {
        return this.INGREDIENT.test(input);
    }

    public ItemStack getInput() {
        return INGREDIENT.getMatchingStacks()[0];
    }

    @Contract(value = "_ -> new", pure = true)
    public ItemStack getOutput(ItemStack input) {
        return OUTPUT.copy();
    }

    /**
     * For JEI, gets a display stack
     *
     * @return Representation of output, MUST NOT be modified
     */
    public List<ItemStack> getOutputDefinition() {
        return OUTPUT.isEmpty() ? Collections.emptyList() : Collections.singletonList(OUTPUT);
    }

    @Override
    public IRecipeSerializer<BlenderRecipe> getSerializer() {
        return new BlenderRecipeSerializer(BlenderRecipe::new);
    }

    public IRecipeType<?> getType() {
        return BlenderRecipeSerializer.BLENDING;
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
        return INGREDIENT.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return OUTPUT.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return OUTPUT.copy();
    }

    @Override
    public boolean isDynamic() {
        //Note: If we make this non dynamic, we can make it show in vanilla's crafting book and also then obey the recipe locking.
        // For now none of that works/makes sense in our concept so don't lock it
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    public BlenderRecipe EMPTY(){
        return this;
    }
}