package net.zargum.plugin.zlib.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta itemMeta;

    /**
     * Init item chainable via given Material parameter.
     *
     * @param itemType the {@link Material} to initiate the instance with.
     * @since 1.0
     */
    public ItemBuilder(final Material itemType) {
        item = new ItemStack(itemType);
        itemMeta = item.getItemMeta();
    }

    /**
     * Init item chainable via given ItemStack parameter.
     *
     * @param itemStack the {@link ItemStack} to initialize the instance with.
     * @since 1.0
     */
    public ItemBuilder(final ItemStack itemStack) {
        item = itemStack;
        itemMeta = item.getItemMeta();
    }

    /**
     * Init the item chainable with no defined Material/ItemStack
     *
     * @since 1.0
     */
    public ItemBuilder() {
        item = new ItemStack(Material.AIR);
        itemMeta = item.getItemMeta();
    }

    /**
     * Changes the Material type of the {@link ItemStack}
     *
     * @param material the new {@link Material} to set for the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder type(final Material material) {
        buildItem().setType(material);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s size.
     *
     * @param itemAmt the new Integer count of the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder amount(final Integer itemAmt) {
        buildItem().setAmount(itemAmt);
        return this;
    }

    /**
     * Changes the {@link ItemStack}s display level.
     *
     * @param name the new String for the ItemStack's display level to be set to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder setName(final String name) {
        meta().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        buildItem().setItemMeta(meta());
        return this;
    }

    /**
     * Adds a line of lore to the {@link ItemStack}
     *
     * @param lore String you want to add to the ItemStack's lore.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder addLoreLine(final String lore) {
        List<String> lores = meta().getLore();
        if (lores == null) {
            lores = new ArrayList<>();
        }
        lores.add('&', lore);
        meta().setLore(lores);
        buildItem().setItemMeta(meta());
        return this;
    }

    /**
     * Clears the {@link ItemStack}s lore and replaces it with the defined String array.
     *
     * @param lores String array you want to set the ItemStack's lore to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder setLore(final String[] lores) {
        List<String> loresList = meta().getLore();
        if (loresList == null) {
            loresList = new ArrayList<>();
        } else {
            loresList.clear();
        }
        Collections.addAll(loresList, lores);
        meta().setLore(loresList);
        return this;
    }

    /**
     * Changes the durability of the current {@link ItemStack}
     *
     * @param durability the new int amount to set the ItemStack's durability to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder setDurability(final int durability) {
        buildItem().setDurability((short) durability);
        return this;
    }

    /**
     * Changes the data value of the {@link ItemStack}
     *
     * @param data the new int data value (parsed as byte) to set the ItemStack's durability to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    @SuppressWarnings("deprecation")
    public ItemBuilder data(final int data) {
        buildItem().setData(new MaterialData(buildItem().getType(), (byte) data));
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@link ItemStack} with a defined level int value.
     *
     * @param enchantment the {@link Enchantment} to add to the ItemStack.
     * @param level       the int amount that the Enchantment's level will be set to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment, final int level) {
        buildItem().addUnsafeEnchantment(enchantment, level);
        return this;
    }

    /**
     * Adds and UnsafeEnchantment to the {@Link} with a level int value of 1.
     *
     * @param enchantment the {@link Enchantment} to add to the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantment(final Enchantment enchantment) {
        buildItem().addUnsafeEnchantment(enchantment, 1);
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack.
     *
     * @param enchantments the Enchantment array to replace any current enchantments applied on the ItemStack.
     * @param level        the int level value for all Enchantments to be set to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments, final int level) {
        buildItem().getEnchantments().clear();
        for (Enchantment enchantment : enchantments) {
            buildItem().addUnsafeEnchantment(enchantment, level);
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the current {@link ItemStack} then adds the defined array of Enchantments to the ItemStack with a level int value of 1.
     *
     * @param enchantments the Enchantment array to replace any current enchantments applied on the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder enchantments(final Enchantment[] enchantments) {
        buildItem().getEnchantments().clear();
        for (Enchantment enchantment : enchantments) {
            buildItem().addUnsafeEnchantment(enchantment, 1);
        }
        return this;
    }

    /**
     * Clears the defined {@link Enchantment} from the {@link ItemStack}
     *
     * @param enchantment the Enchantment to delete from the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantment(final Enchantment enchantment) {
        Map<Enchantment, Integer> itemEnchantments = buildItem().getEnchantments();
        for (Enchantment enchantmentC : itemEnchantments.keySet()) {
            if (enchantment == enchantmentC) {
                itemEnchantments.remove(enchantmentC);
            }
        }
        return this;
    }

    /**
     * Clears all {@link Enchantment}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearEnchantments() {
        buildItem().getEnchantments().clear();
        return this;
    }

    /**
     * Clears the defined {@link String} of lore from the {@link ItemStack}
     *
     * @param lore the String to be removed from the ItemStack.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearLore(final String lore) {
        meta().getLore().remove(lore);
        buildItem().setItemMeta(meta());
        return this;
    }

    /**
     * Clears all lore {@link String}s from the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearLores() {
        meta().getLore().clear();
        buildItem().setItemMeta(meta());
        return this;
    }

    /**
     * Sets the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @param color the Color to set the LEATHER_ARMOR ItemStack to.
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder color(final Color color) {
        if (buildItem().getType() == Material.LEATHER_HELMET
                || buildItem().getType() == Material.LEATHER_CHESTPLATE
                || buildItem().getType() == Material.LEATHER_LEGGINGS
                || buildItem().getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(color);
            buildItem().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Clears the {@link Color} of any LEATHER_ARMOR {@link Material} types of the {@link ItemStack}
     *
     * @return the current instance for chainable application.
     * @since 1.0
     */
    public ItemBuilder clearColor() {
        if (buildItem().getType() == Material.LEATHER_HELMET
                || buildItem().getType() == Material.LEATHER_CHESTPLATE
                || buildItem().getType() == Material.LEATHER_LEGGINGS
                || buildItem().getType() == Material.LEATHER_BOOTS) {
            LeatherArmorMeta meta = (LeatherArmorMeta) meta();
            meta.setColor(null);
            buildItem().setItemMeta(meta);
        }
        return this;
    }

    /**
     * Sets the skullOwner {@link SkullMeta} of the current SKULL_ITEM {@link Material} type {@link ItemStack}
     *
     * @param name the {@link String} value to set the SkullOwner meta to for the SKULL_ITEM Material type ItemStack.
     * @return the current instance for chainable application
     * @since 1.0
     */
    public ItemBuilder skullOwner(final String name) {
        if (buildItem().getType() == Material.SKULL_ITEM && buildItem().getDurability() == (byte) 3) {
            SkullMeta skullMeta = (SkullMeta) meta();
            skullMeta.setOwner(name);
            buildItem().setItemMeta(meta());
        }
        return this;
    }

    /**
     * Returns the {@link ItemMeta} of the {@link ItemStack}
     *
     * @return the ItemMeta of the ItemStack.
     */
    public ItemMeta meta() {
        return itemMeta;
    }

    /**
     * Returns the {@link ItemStack} of the {@link ItemBuilder} instance.
     *
     * @return the ItemStack of the ItemBuilder instance.
     */
    public ItemStack buildItem() {
        return item;
    }

}
