package io.screret.github.juicesandsodas.util;

import io.screret.github.juicesandsodas.crafting.BlenderRecipeSerializer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.AbstractCookingRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BlenderRecipe extends AbstractCookingRecipe {

    protected final IRecipeType<?> type;
    protected final ResourceLocation id;
    public final String group;
    public final Ingredient ingredient;
    public final ItemStack result;
    public final float experience;
    public final int cookTime;

    public BlenderRecipe(ResourceLocation idIn, String groupIn, Ingredient ingredientIn, ItemStack resultIn, float experienceIn, int cookTimeIn) {
        super(IRecipeType.register("blending"), idIn, groupIn, ingredientIn, resultIn, experienceIn, cookTimeIn);
        this.type = IRecipeType.register("blending");
        this.id = idIn;
        this.group = groupIn;
        this.ingredient = ingredientIn;
        this.result = resultIn;
        this.experience = experienceIn;
        this.cookTime = cookTimeIn;
    }

    @Override
    public String toString () {

        // All vanilla recipe types return their ID in toString. I am not sure how vanilla uses
        // this, or if it does. Modded types should follow this trend for the sake of
        // consistency. I am also using it during registry to create the ResourceLocation ID.
        return "juicesandsodas:blending";
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        return this.ingredient.test(inv.getStackInSlot(0))
                && this.ingredient.test(inv.getStackInSlot(1))
                && this.ingredient.test(inv.getStackInSlot(2));
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }
    /**
     * Recipes with equal group are combined into one button in the recipe book
     */
    public String getGroup() {
        return this.group;
    }

    /**
     * Gets the cook time in ticks
     */
    public int getCookTime() {
        return this.cookTime;
    }

    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return new BlenderRecipeSerializer(BlenderRecipe::new, 150);
    }

    public IRecipeType<?> getType() {
        return this.type;
    }

    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonnullList = NonNullList.create();
        nonnullList.add(this.ingredient);
        return nonnullList;
    }
}
