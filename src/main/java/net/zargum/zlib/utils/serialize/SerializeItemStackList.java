package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializeItemStackList {

    public static JsonArray itemsToJson(ItemStack[] items){
        JsonArray values = new JsonArray();
        for (ItemStack item : items) {
            if (item == null) continue;
            JsonObject jsonItem = SingleItemSerialization.itemToJson(item);
            values.add(jsonItem);
        }
        return values;
    }

    public static ItemStack[] jsonToItems(String jsonString){
        JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        JsonArray jsonArray = new JsonArray();
        for (Map.Entry<String, JsonElement> json : jsonObject.entrySet()) jsonArray.add(json.getValue());
        return jsonToItems(jsonArray);
    }

    public static ItemStack[] jsonToItems(JsonArray jsonArray){
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonItem = jsonArray.get(i).getAsJsonObject();
            items.add(SingleItemSerialization.jsonToItem(jsonItem));
        }
        return items.toArray(new ItemStack[0]);
    }

    public static Map<Integer, ItemStack> jsonToItemsWithSlot(JsonArray jsonArray){
        Map<Integer, ItemStack> items = new HashMap<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonItem = jsonArray.get(i).getAsJsonObject();

            int slot = jsonItem.get("slot").getAsInt();
            ItemStack item = SingleItemSerialization.jsonToItem(jsonItem);
            items.put(slot, item);
        }
        return items;
    }

}
