package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class FireworkSerialization {

	public static FireworkMeta JsonToFireworkmeta(JsonObject json) {
		FireworkMeta dummy = (FireworkMeta) new ItemStack(Material.FIREWORK).getItemMeta();
		dummy.setPower(json.get("power").getAsInt());
		JsonArray effects = json.getAsJsonArray("effects");
		for(int i = 0; i < effects.size(); i++) {
			JsonObject effectDto = effects.get(i).getAsJsonObject();
			FireworkEffect effect = FireworkEffectSerialization.JsonToFireworkeffect(effectDto);
			if(effect != null) dummy.addEffect(effect);
		}
		return dummy;
	}
	
	public static JsonObject FireworkmetaToJson(FireworkMeta meta) {
		JsonObject root = new JsonObject();
		root.addProperty("power", meta.getPower());
		JsonArray effects = new JsonArray();
		for(FireworkEffect e : meta.getEffects()) effects.add(FireworkEffectSerialization.FireworkeffectToJson(e));
		root.add("effects", effects);
		return root;
	}

}
