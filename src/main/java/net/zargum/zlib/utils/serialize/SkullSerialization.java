package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class SkullSerialization {

    public static JsonObject SkullmetaToJson(SkullMeta meta) {
        JsonObject jsonObject = new JsonObject();
        if (meta.hasOwner()) jsonObject.addProperty("owner", meta.getOwner());
        return jsonObject;
    }

    public static SkullMeta getSkullMeta(String jsonString) {
        return getSkullMeta(new JsonParser().parse(jsonString).getAsJsonObject());
    }
    public static SkullMeta getSkullMeta(JsonObject jsonObject) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM);
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
        if (jsonObject.has("owner")) skullMeta.setOwner(jsonObject.get("owner").getAsString());
        return skullMeta;
    }
}
