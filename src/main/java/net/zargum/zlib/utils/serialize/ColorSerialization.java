package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonObject;
import org.bukkit.Color;

public class ColorSerialization {

	public static JsonObject serializeColor(Color color) {
		JsonObject root = new JsonObject();
		root.addProperty("red", color.getRed());
		root.addProperty("green", color.getGreen());
		root.addProperty("blue", color.getBlue());
		return root;
	}

	public static Color getColor(String string) {
		return getColor(new JsonObject().get(string).getAsString());
	}

	public static Color getColor(JsonObject jsonObject) {
		int r = 0, g = 0, b = 0;
		if(jsonObject.has("red")) r = jsonObject.get("red").getAsInt();
		if(jsonObject.has("green")) g = jsonObject.get("green").getAsInt();
		if(jsonObject.has("blue")) b = jsonObject.get("blue").getAsInt();
		return Color.fromRGB(r, g, b);
	}

	public static String serializeColorAsString(Color color) {
		return serializeColor(color).toString();
	}

}
