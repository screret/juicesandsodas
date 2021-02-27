package io.screret.github.juicesandsodas.crafting;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.screret.github.juicesandsodas.util.BlenderRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class BlenderRecipeSerializer<T extends BlenderRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlenderRecipe> {

    public static Map<Ingredient, ItemStack> RECIPES = Maps.newHashMap();
    public static IRecipeType<BlenderRecipe> BLENDING = IRecipeType.register("blending");

    private final IFactory<T> factory;

    public static ItemStack input;
    public static ItemStack output;
    public static ResourceLocation id;

    private static final Logger LOGGER = LogManager.getLogger();

    public BlenderRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public BlenderRecipe read(ResourceLocation recipeId, JsonObject json) {
        JsonElement jsonelement = JSONUtils.isJsonArray(json, "ingredient") ? JSONUtils.getJsonArray(json, "ingredient") : JSONUtils.getJsonObject(json, "item");
        Ingredient ingredient = Ingredient.deserialize(jsonelement);
        //Forge: Check if primitive string to keep vanilla or a object which can contain a count field.
        if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack itemstack;
        if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
        else {
            String s1 = JSONUtils.getString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
        }
        input = ingredient.getMatchingStacks()[0];
        output = itemstack;
        id = recipeId;
        RECIPES.put(ingredient, output);
        LOGGER.debug("Loaded " + this.toString());

        return this.factory.create(recipeId, ingredient, itemstack);
    }

    @Override
    public BlenderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack itemstack = buffer.readItemStack();
        return this.factory.create(recipeId, ingredient, itemstack);
    }

    @Override
    public void write(PacketBuffer buffer, BlenderRecipe recipe) {
        recipe.INGREDIENT.write(buffer);
        buffer.writeItemStack(recipe.OUTPUT);
    }

    public interface IFactory<RECIPE extends BlenderRecipe> {
        RECIPE create(ResourceLocation p_create_1_, Ingredient p_create_3_, ItemStack p_create_4_);
    }

    @Override
    public String toString() {
        // Overriding toString is not required, it's just useful for debugging.
        return String.format("BlenderRecipe [input=%s, output=%s, id=%s]", input, output, id);
    }
}
