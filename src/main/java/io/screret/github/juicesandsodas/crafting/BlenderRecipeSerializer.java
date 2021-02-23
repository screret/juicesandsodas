package io.screret.github.juicesandsodas.crafting;

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

public class BlenderRecipeSerializer<T extends BlenderRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BlenderRecipe> {

    public static IRecipeType<BlenderRecipe> BLENDING = IRecipeType.register("blending");

    private final int cookingTime;
    private final IFactory<T> factory;

    public static ItemStack input;
    public static ItemStack output;
    public static ResourceLocation id;

    private static final Logger LOGGER = LogManager.getLogger();

    public BlenderRecipeSerializer(IFactory<T> factory, int cookingTime) {
        this.cookingTime = cookingTime;
        this.factory = factory;
    }

    public BlenderRecipe read(ResourceLocation recipeId, JsonObject json) {
        String s = JSONUtils.getString(json, "group", "");
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
        float f = JSONUtils.getFloat(json, "experience", 0.0F);
        int i = JSONUtils.getInt(json, "cookingtime", this.cookingTime);
        input = ingredient.getMatchingStacks()[0];
        output = itemstack;
        id = recipeId;
        LOGGER.debug("Loaded " + this.toString());
        return new BlenderRecipe(recipeId, s, ingredient, itemstack, f, i);
    }

    public BlenderRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
        String s = buffer.readString(32767);
        Ingredient ingredient = Ingredient.read(buffer);
        ItemStack itemstack = buffer.readItemStack();
        float f = buffer.readFloat();
        int i = buffer.readVarInt();
        return new BlenderRecipe(recipeId, s, ingredient, itemstack, f, i);
    }

    @Override
    public void write(PacketBuffer buffer, BlenderRecipe recipe) {
        recipe.ingredient.write(buffer);
        buffer.writeItemStack(recipe.result);
        buffer.writeFloat(recipe.experience);
    }

    public interface IFactory<T extends BlenderRecipe> {
        T create(ResourceLocation p_create_1_, String p_create_2_, Ingredient p_create_3_, ItemStack p_create_4_, float p_create_5_, int p_create_6_);
    }

    @Override
    public String toString() {
        // Overriding toString is not required, it's just useful for debugging.
        return String.format("BlenderRecipe [input=%s, output=%s, id=%s]", input, output, id);
    }
}
