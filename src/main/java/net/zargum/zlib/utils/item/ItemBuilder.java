package net.zargum.zlib.utils.item;

import net.zargum.zlib.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.*;

public class ItemBuilder {

    protected final ItemStack item;

    public ItemBuilder(Material material) {
        item = new ItemStack(material);
        if (item.getAmount() == 0) item.setAmount(1);
    }

    public ItemBuilder(ItemStack itemStack) {
        item = itemStack;
        if (item.getAmount() == 0) item.setAmount(1);
    }

    public ItemBuilder setItemMeta(ItemMeta meta) {
        item.setItemMeta(meta);
        return this;
    }

    public ItemMeta getItemMeta() {
        return item.getItemMeta();
    }

    public ItemBuilder setType(Material material) {
        item.setType(material);
        return this;
    }

    public Material getType() {
        return item.getType();
    }

    public ItemBuilder setAmount(Integer amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        name = name.isEmpty() ? " " : name;
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

    public ItemBuilder addLore(String... lore) {
        ItemMeta itemMeta = getItemMeta();
        List<String> lores = itemMeta.getLore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.addAll(Arrays.asList(ColorUtils.translate(lore)));
        itemMeta.setLore(lores);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLore(int line) {
        ItemMeta itemMeta = getItemMeta();
        itemMeta.getLore().remove(line);
        item.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder removeLore() {
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

    public ItemBuilder setDurability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setData(int data) {
        item.setData(new MaterialData(item.getType(), (byte) data));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean unbreakable) {
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(meta);
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
