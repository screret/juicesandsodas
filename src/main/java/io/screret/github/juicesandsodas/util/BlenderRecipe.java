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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Inputs: Igredient (item) Output: ItemStack (transformed)
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlenderRecipe implements IRecipe<IInventory> {

    public Ingredient ingredient;
    public ItemStack output;

    public ResourceLocation id;



    private final Logger logger = LogManager.getLogger();

    public BlenderRecipe(ResourceLocation id, Ingredient input, ItemStack output) {
        this.ingredient = input;
        this.output = output.copy();
        this.id = id;

        logger.debug("Loaded " + this.toString());
    }

    public boolean test(ItemStack input) {
        return ingredient.test(input);
    }

    public Ingredient getInput() {
        return ingredient;
    }

    @Contract(value = "_ -> new", pure = true)
    public ItemStack getOutput(Ingredient input) {
        return output.copy();
    }

    @Override
    public IRecipeSerializer<BlenderRecipe> getSerializer() {
        return BlenderRecipeSerializer.SERIALIZER;
    }

    public IRecipeType<?> getType() {
        return BlenderRecipeSerializer.BLENDING;
    }

    @Override
    public boolean matches(@Nonnull IInventory inv, @Nonnull World world) {
        return ingredient.test(inv.getStackInSlot(0));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return output.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output.copy();
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

    @Override
    public String toString() {
        // Overriding toString is not required, it's just useful for debugging.
        return String.format("BlenderRecipe [input=%s, output=%s, id=%s]", ingredient.getMatchingStacks()[0], output, id);
    }
}