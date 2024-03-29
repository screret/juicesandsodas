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

    public Map<Ingredient, ItemStack> RECIPES = Maps.newHashMap();
    public static IRecipeType<BlenderRecipe> BLENDING = IRecipeType.register("juicesandsodas:blending");
    public static IRecipeSerializer<BlenderRecipe> SERIALIZER;

    private final IFactory<T> factory;

    public Ingredient input;
    public ItemStack output;
    public ResourceLocation id;

    private final Logger logger = LogManager.getLogger();

    public BlenderRecipeSerializer(IFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public BlenderRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
        JsonElement jsonelement = JSONUtils.isValidNode(json, "ingredient") ? JSONUtils.getAsJsonArray(json, "ingredient") : JSONUtils.getAsJsonObject(json, "item");
        Ingredient ingredient = Ingredient.fromJson(jsonelement);
        if (!json.has("result")) throw new com.google.gson.JsonSyntaxException("Missing result, expected to find a string or object");
        ItemStack itemstack;
        if (json.get("result").isJsonObject()) itemstack = ShapedRecipe.itemFromJson(JSONUtils.getAsJsonObject(json, "result"));
        else {
            String s1 = JSONUtils.getAsString(json, "result");
            ResourceLocation resourcelocation = new ResourceLocation(s1);
            itemstack = new ItemStack(Registry.ITEM.getOptional(resourcelocation).orElseThrow(() -> new IllegalStateException("Item: " + s1 + " does not exist")));
        }
        input = ingredient;
        output = itemstack;
        id = recipeId;
        RECIPES.put(ingredient, output);
        return this.factory.create(recipeId, ingredient, itemstack);
    }

    @Override
    public BlenderRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
        Ingredient ingredient = Ingredient.fromNetwork(buffer);
        ItemStack itemstack = buffer.readItem();
        return this.factory.create(recipeId, ingredient, itemstack);
    }

    @Override
    public void toNetwork(PacketBuffer buffer, BlenderRecipe recipe) {
        recipe.ingredient.toNetwork(buffer);
        buffer.writeItemStack(recipe.output, false);
    }

    public interface IFactory<RECIPE extends BlenderRecipe> {
        RECIPE create(ResourceLocation p_create_1_, Ingredient p_create_3_, ItemStack p_create_4_);
    }
}
