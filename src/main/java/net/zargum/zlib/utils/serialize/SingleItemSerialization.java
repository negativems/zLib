package net.zargum.zlib.utils.serialize;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SingleItemSerialization {

    /**
     *
     * Get JsonObject from ItemStack
     *
     */
    public static JsonObject itemToJson(ItemStack items) {
        return itemToJson(items, false, 0);
    }

    public static JsonObject itemToJson(ItemStack items, int index) {
        return itemToJson(items, true, index);
    }

    public static JsonObject itemToJson(ItemStack item, boolean useIndex, int index) {

        if (item == null) return null;
        JsonObject values = new JsonObject();

        //Setting variables
        int id = item.getTypeId();
        int amount = item.getAmount();
        int data = item.getDurability();
        int repairPenalty = 0;
        Material material = item.getType();
        String name = null;
        String enchants = null;
        List<String> lore = null;
        JsonObject bookMeta = null;
        JsonObject armorMeta = null;
        JsonObject skullMeta = null;
        JsonObject fwMeta = null;

        //Set value to variables
        if (material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK)
            bookMeta = BookSerialization.serializeBookMeta((BookMeta) item.getItemMeta());
        else if (material == Material.ENCHANTED_BOOK)
            bookMeta = BookSerialization.serializeEnchantedBookMeta((EnchantmentStorageMeta) item.getItemMeta());
        else if (Util.isLeatherArmor(material))
            armorMeta = LeatherArmorSerialization.serializeArmor((LeatherArmorMeta) item.getItemMeta());
        else if (material == Material.SKULL_ITEM)
            skullMeta = SkullSerialization.SkullmetaToJson((SkullMeta) item.getItemMeta());
        else if (material == Material.FIREWORK)
            fwMeta = FireworkSerialization.FireworkmetaToJson((FireworkMeta) item.getItemMeta());
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasDisplayName()) name = meta.getDisplayName();
            if (meta.hasLore()) lore = meta.getLore();
            if (meta.hasEnchants()) enchants = EnchantmentSerialization.serializeEnchantments(meta.getEnchants());
            if (meta instanceof Repairable && ((Repairable) meta).hasRepairCost())
                repairPenalty = ((Repairable) meta).getRepairCost();
        }

        //Add variables to the JsonObject
        values.addProperty("id", id);
        values.addProperty("amount", amount);
        values.addProperty("data", data);
        if (useIndex) values.addProperty("index", index);
        if (name != null) values.addProperty("level", name);
        if (enchants != null) values.addProperty("enchantments", enchants);
        if (lore != null) values.add("lore", new JsonObject().getAsJsonArray(new GsonBuilder().create().toJson(lore)));
        if (repairPenalty != 0) values.addProperty("repairPenalty", repairPenalty);
        if (bookMeta != null && bookMeta.getAsJsonArray().size() > 0) values.add("book-meta", bookMeta);
        if (armorMeta != null && armorMeta.getAsJsonArray().size() > 0) values.add("armor-meta", armorMeta);
        if (skullMeta != null && skullMeta.getAsJsonArray().size() > 0) values.add("skull-meta", skullMeta);
        if (fwMeta != null && fwMeta.getAsJsonArray().size() > 0) values.add("firework-meta", fwMeta);

        return values;
    }

    /**
     *
     * Get ItemStack from json String.
     *
     */
    public static ItemStack jsonToItem(String jsonString) {
        return jsonToItem(new JsonParser().parse(jsonString).getAsJsonObject(), 0);
    }
    public static ItemStack jsonToItem(JsonObject jsonObject) {
        return jsonToItem(jsonObject, 0);
    }
    public static ItemStack jsonToItem(JsonObject jsonObject, int itemId) {

        int id = jsonObject.get("id").getAsInt();
        int amount = jsonObject.get("amount").getAsInt();
        int data = jsonObject.get("data").getAsInt();
        int repairPenalty = 0;
        String name = null;
        Map<Enchantment, Integer> enchants = null;
        ArrayList<String> lore = null;
        if (jsonObject.has("level")) name = jsonObject.get("level").getAsString();
        if (jsonObject.has("enchantments")) enchants = EnchantmentSerialization.getEnchantments(jsonObject.get("enchantments").getAsString());
        if (jsonObject.has("lore")) {
            lore = new ArrayList<>();
            JsonArray l = jsonObject.getAsJsonArray("lore");
            for (int j = 0; j < l.size(); j++) lore.add(l.get(j).getAsString());
        }

        if (jsonObject.has("repairPenalty")) repairPenalty = jsonObject.get("repairPenalty").getAsInt();
        if (Material.getMaterial(id) == null) throw new IllegalArgumentException("SingleItemSerialization - No Material found with id " + id);
        Material material = Material.getMaterial(id);
        ItemStack item = new ItemStack(material, amount, (short) data);
        if ((material == Material.BOOK_AND_QUILL || material == Material.WRITTEN_BOOK) && jsonObject.has("book-meta")) {
            BookMeta meta = BookSerialization.getBookMeta(jsonObject.getAsJsonObject("book-meta"));
            item.setItemMeta(meta);
        } else if (material == Material.ENCHANTED_BOOK && jsonObject.has("book-meta")) {
            EnchantmentStorageMeta meta = BookSerialization.getEnchantedBookMeta(jsonObject.getAsJsonObject("book-meta"));
            item.setItemMeta(meta);
        } else if (Util.isLeatherArmor(material) && jsonObject.has("armor-meta")) {
            LeatherArmorMeta meta = LeatherArmorSerialization.getLeatherArmorMeta(jsonObject.getAsJsonObject("armor-meta"));
            item.setItemMeta(meta);
        } else if (material == Material.SKULL_ITEM && jsonObject.has("skull-meta")) {
            SkullMeta meta = SkullSerialization.getSkullMeta(jsonObject.getAsJsonObject("skull-meta"));
            item.setItemMeta(meta);
        } else if (material == Material.FIREWORK && jsonObject.has("firework-meta")) {
            FireworkMeta meta = FireworkSerialization.JsonToFireworkmeta(jsonObject.getAsJsonObject("firework-meta"));
            item.setItemMeta(meta);
        }

        ItemMeta meta = item.getItemMeta();
        if (name != null) meta.setDisplayName(name);
        if (lore != null) meta.setLore(lore);
        item.setItemMeta(meta);
        if (repairPenalty != 0) {
            Repairable rep = (Repairable) meta;
            rep.setRepairCost(repairPenalty);
            item.setItemMeta((ItemMeta) rep);
        }

        if (enchants != null) item.addUnsafeEnchantments(enchants);
        return item;
    }

}
