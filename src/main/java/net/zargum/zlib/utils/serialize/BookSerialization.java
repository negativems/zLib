package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class to help with the serialization of books. There are separate methods provided for the ItemMeta
 * of enchanted books and written books
 * @author KILL3RTACO
 * @since 1.0
 *
 */
public class BookSerialization {

    protected BookSerialization() {
    }

    /**
     * Get BookMeta from a JSON string
     * @param stringJson The JSON string that a JsonObject will be constructed from
     * @return The BookMeta constructed, or null if an error occurs
     */
    public static BookMeta getBookMeta(String stringJson) {
        return getBookMeta(new JsonParser().parse(stringJson).getAsJsonObject());
    }

    /**
     * Get BookMeta from a JsonObject.
     * @param json
     * @return The BookMeta constructed, or null if an error occurs
     */
    public static BookMeta getBookMeta(JsonObject json) {
        ItemStack dummyItems = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta meta = (BookMeta) dummyItems.getItemMeta();
        String title = null, author = null;
        JsonArray pages = null;

        if(json.has("title")) title = json.get("title").getAsString();
        if(json.has("author")) author = json.get("author").getAsString();
        if(json.has("pages")) pages = json.get("pages").getAsJsonArray();
        if(title != null) meta.setTitle(title);
        if(author != null) meta.setAuthor(author);

        if(pages != null) {
            List<String> pagesList = new ArrayList<>();
            while (pages.iterator().hasNext()) pagesList.add(pages.iterator().next().getAsString());
            meta.setPages(pagesList);
        }

        return meta;
    }

    /**
     * Gets a JsonObject representation of a BookMeta. Book and Quills books will have a pages key, while
     * finished, written, books will also have an author and title key.
     * @param meta The BookMeta to serialize
     * @return A JSON Representation of the give BookMeta
     */
    public static JsonObject serializeBookMeta(BookMeta meta) {
        JsonObject jsonObject = new JsonObject();
        if(meta.hasTitle()) jsonObject.addProperty("title", meta.getTitle());
        if(meta.hasAuthor()) jsonObject.addProperty("author", meta.getAuthor());
        if(meta.hasPages()) {
            JsonArray pages = new JsonArray();
            while (meta.getPages().iterator().hasNext()) pages.add((JsonElement) meta.getPages());
            jsonObject.addProperty("pages", pages.getAsString());
        }
        return jsonObject;
    }

    /**
     * Serialize BookMeta. This will produce the same result as serializeBookMeta(meta).toString()
     * @param meta The BookMeta to serialize.
     * @return The serialization string
     */
    public static String serializeBookMetaAsString(BookMeta meta) {
        return serializeBookMeta(meta).toString();
    }

    /**
     * Get EnchantmentStorageMeta from a JSON string
     * @param jsonString The string to use
     * @return The EnchantmentStorageMeta constructed, null if an error occurred
     */
    public static EnchantmentStorageMeta getEnchantedBookMeta(String jsonString) {
        return getEnchantedBookMeta(new JsonParser().parse(jsonString).getAsJsonObject());
    }

    /**
     * Get EncantmentStorageMeta from a JsonObject.
     * @param json The JsonObject to use
     * @return The EnchantmentStorageMeta constructed, null if an error occurred
     */
    public static EnchantmentStorageMeta getEnchantedBookMeta(JsonObject json) {
        ItemStack dummyItems = new ItemStack(Material.ENCHANTED_BOOK, 1);
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) dummyItems.getItemMeta();
        if(json.has("enchantments")) {
            Map<Enchantment, Integer> enchants = EnchantmentSerialization.getEnchantments(json.get("enchantments").getAsString());
            for(Enchantment e : enchants.keySet()) {
                meta.addStoredEnchant(e, enchants.get(e), true);
            }
        }
        return meta;
    }

    /**
     * Serialize EnchantmentStorageMeta into JsonObject form.
     * @param meta The EnchantmentStorageMeta to serialize
     * @return The JsonObject form of the given EnchantmentStorageMeta
     */
    public static JsonObject serializeEnchantedBookMeta(EnchantmentStorageMeta meta) {
        JsonObject root = new JsonObject();
        String enchants = EnchantmentSerialization.serializeEnchantments(meta.getStoredEnchants());
        root.addProperty("enchantments", enchants);
        return root;
    }
}
