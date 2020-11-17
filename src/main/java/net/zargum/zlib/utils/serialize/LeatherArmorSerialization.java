package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorSerialization {
	
	protected LeatherArmorSerialization() {
	}

	public static JsonObject serializeArmor(LeatherArmorMeta meta) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("color", ColorSerialization.serializeColor(meta.getColor()).getAsString());
		return jsonObject;
	}

	public static String serializeArmorAsString(LeatherArmorMeta meta) {
		return serializeArmor(meta).toString();
	}

	public static LeatherArmorMeta getLeatherArmorMeta(String jsonString) {
		return getLeatherArmorMeta(new JsonParser().parse(jsonString).getAsJsonObject());
	}

	public static LeatherArmorMeta getLeatherArmorMeta(JsonObject json) {
		ItemStack dummyItems = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta meta = (LeatherArmorMeta) dummyItems.getItemMeta();
		if(json.has("color")) meta.setColor(ColorSerialization.getColor(json.get("color").getAsJsonObject()));
		return meta;
	}
}
