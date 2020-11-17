package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventorySerialization {

	public static JsonArray inventoryToJsonArray(ItemStack[] contents) {
		JsonArray inventory = new JsonArray();
		for(int i = 0; i < contents.length; i++) {
			JsonObject values = SingleItemSerialization.itemToJson(contents[i], i);
			if(values != null) inventory.add(values);
		}
		return inventory;
	}
	public static JsonArray inventoryToJsonArray(Inventory inv) {
		return inventoryToJsonArray(inv.getContents());
	}
	public static JsonObject playerInventoryToJson(PlayerInventory inv) {
		JsonObject jsonObject = new JsonObject();
		JsonArray inventory = inventoryToJsonArray(inv);
		JsonArray armor = inventoryToJsonArray(inv.getArmorContents());
		jsonObject.add("inventory", inventory);
		jsonObject.add("armor", armor);
		return jsonObject;
	}
	public static String playerInventoryToString(PlayerInventory inv) {
		return playerInventoryToJson(inv).toString();
	}
	public static String inventoryToString(Inventory inventory) {
		return inventoryToJsonArray(inventory).toString();
	}
	public static String itemStacksToString(ItemStack[] contents) {
		return inventoryToJsonArray(contents).toString();
	}
	public static ItemStack[] getInventory(JsonArray inv, int size) {
		ItemStack[] contents = new ItemStack[size];
		for(int i = 0; i < inv.size(); i++) {
			JsonObject item = inv.get(i).getAsJsonObject();
			int index = item.get("index").getAsInt();
			if(index > size) throw new IllegalArgumentException("index found is greator than expected size (" + index + ">" + size + ")");
			if(index > contents.length || index < 0) throw new IllegalArgumentException("Item " + i + " - Slot " + index + " does not exist in this inventory");
			ItemStack stuff = SingleItemSerialization.jsonToItem(item);
			contents[index] = stuff;
		}
		return contents;
	}
	public static void setInventory(Inventory inventory, JsonArray inv) {
		ItemStack[] items = getInventory(inv, inventory.getSize());
		inventory.clear();
		for(int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			if(item == null)
				continue;
			inventory.setItem(i, item);
		}
	}
}
