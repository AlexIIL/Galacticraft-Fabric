package io.github.teamgalacticraft.galacticraft.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.crafting.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

import java.util.Iterator;
import java.util.Map;

/**
 * @author <a href="https://github.com/teamgalacticraft">TeamGalacticraft</a>
 */
public class ShapedCompressingRecipeSerializer<T extends ShapedCompressingRecipe> implements RecipeSerializer<T> {
    private final RecipeFactory<T> factory;

    public ShapedCompressingRecipeSerializer(RecipeFactory<T> factory) {
        this.factory = factory;
    }

    @Override
    public T read(Identifier identifier_1, JsonObject jsonObject_1) {
        String string_1 = JsonHelper.getString(jsonObject_1, "group", "");
        Map<String, Ingredient> map_1 = ShapedCompressingRecipe.getComponents(JsonHelper.getObject(jsonObject_1, "key"));
        String[] strings_1 = ShapedCompressingRecipe.combinePattern(ShapedCompressingRecipe.getPattern(JsonHelper.getArray(jsonObject_1, "pattern")));
        int int_1 = strings_1[0].length();
        int int_2 = strings_1.length;
        DefaultedList<Ingredient> defaultedList_1 = ShapedCompressingRecipe.getIngredients(strings_1, map_1, int_1, int_2);
        ItemStack itemStack_1 = ShapedRecipe.getItemStack(JsonHelper.getObject(jsonObject_1, "result"));
        return factory.create(identifier_1, string_1, int_1, int_2, defaultedList_1, itemStack_1);
    }

    @Override
    public T read(Identifier identifier_1, PacketByteBuf packet) {
        int int_1 = packet.readVarInt();
        int int_2 = packet.readVarInt();
        String group = packet.readString(32767);
        DefaultedList<Ingredient> defaultedList_1 = DefaultedList.create(int_1 * int_2, Ingredient.EMPTY);

        for (int int_3 = 0; int_3 < defaultedList_1.size(); ++int_3) {
            defaultedList_1.set(int_3, Ingredient.fromPacket(packet));
        }

        ItemStack itemStack_1 = packet.readItemStack();
        return factory.create(identifier_1, group, int_1, int_2, defaultedList_1, itemStack_1);
    }

    @Override
    public void write(PacketByteBuf packet, T recipe) {
        packet.writeVarInt(recipe.getWidth());
        packet.writeVarInt(recipe.getHeight());
        packet.writeString(recipe.getGroup());
        Iterator var3 = recipe.getIngredients().iterator();

        while (var3.hasNext()) {
            Ingredient ingredient_1 = (Ingredient) var3.next();
            ingredient_1.write(packet);
        }

        packet.writeItemStack(recipe.getOutput());
    }

    interface RecipeFactory<T extends ShapedCompressingRecipe> {
        T create(Identifier id, String group, int int_1, int int_2, DefaultedList<Ingredient> input, ItemStack output);
    }
}