package net.zargum.zlib.utils.item;

import net.zargum.zlib.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    protected final ItemStack item;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
    }

    public ItemBuilder(ItemStack itemStack) {
        item = itemStack;
    }

    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    public ItemBuilder type(Material material) {
        item.setType(material);
        return this;
    }

    public ItemBuilder amount(Integer amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setName(String name) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        lore = ColorUtils.translate(lore);
        ItemMeta itemMeta = item.getItemMeta();
        List<String> loresList = itemMeta.getLore();
        if (loresList == null) loresList = new ArrayList<>();
        else loresList.clear();

        Collections.addAll(loresList, lore);
        itemMeta.setLore(loresList);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        setLore(lore.toArray(new String[0]));
        return this;
    }

    public ItemBuilder addLoreLine(String lore) {
        ItemMeta itemMeta = getItemMeta();
        List<String> lores = itemMeta.getLore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.add(ColorUtils.translate(lore));
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder clearLore(int line) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getLore().remove(line);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder clearLore() {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getLore().clear();
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder replaceLoreVariables(String variable, String value) {
        ItemMeta itemMeta = getItemMeta();
        List<String> lore = itemMeta.getLore();
        for (int i = 0; i < lore.size(); i++) {
            String line = lore.get(i);
            lore.set(i, line.replaceAll("%" + variable + "%", value));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder durability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    @SuppressWarnings("deprecation")
    public ItemBuilder data(int data) {
        item.setData(new MaterialData(item.getType(), (byte) data));
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment) {
        item.addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    public ItemBuilder enchant(Enchantment[] enchantments, int level) {
        for (Enchantment enchantment : enchantments) {
            item.addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    public ItemBuilder enchant(Enchantment[] enchantments) {
        for (Enchantment enchantment : enchantments) {
            item.addUnsafeEnchantment(enchantment, 1);
        }
        return this;
    }

    public ItemBuilder removeEnchantment(Enchantment enchantment) {
        ItemMeta itemMeta = getItemMeta();
        Map<Enchantment, Integer> itemEnchantments = item.getEnchantments();
        for (Enchantment enchantmentC : itemEnchantments.keySet()) {
            if (enchantment == enchantmentC) {
                itemMeta.removeEnchant(enchantmentC);
            }
        }
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder clearEnchantments() {
        item.getEnchantments().clear();
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(flags);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder removeFlags(ItemFlag... flags) {
        ItemMeta meta = item.getItemMeta();
        meta.removeItemFlags(flags);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder removeFlags() {
        ItemMeta meta = item.getItemMeta();
        meta.getItemFlags().forEach(this::removeFlags);
        item.setItemMeta(meta);

        return this;
    }

    public ItemBuilder setMetadata(String key, Object value) {
        MetadataUtils.setMetadata(item,key, value);
        return this;
    }

    public ItemBuilder removeMetadata(String key) {
        MetadataUtils.setMetadata(item, key, null);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(item.getItemMeta());
        return item;
    }

}
