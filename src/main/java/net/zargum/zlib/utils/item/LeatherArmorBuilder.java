package net.zargum.zlib.utils.item;

import net.zargum.zlib.utils.JavaUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class LeatherArmorBuilder extends ItemBuilder{

    public LeatherArmorBuilder(ItemStack item) {
        super(item);
    }

    public LeatherArmorBuilder(Material material) {
        super(material);
    }

    @Override
    public LeatherArmorMeta getItemMeta() {
        return (LeatherArmorMeta) item.getItemMeta();
    }

    public LeatherArmorBuilder color(Color color) {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(color);
        item.setItemMeta(meta);
        return this;
    }

    public LeatherArmorBuilder color(int rgb) {
        return color(Color.fromRGB(rgb));
    }

    public LeatherArmorBuilder color(int red, int green, int blue) {
        return color(Color.fromRGB(red, green, blue));
    }

    public LeatherArmorBuilder color(String hex) {
        if (!JavaUtils.isHexNumber(hex.substring(1)) || hex.length() != 7) throw new IllegalStateException("Invalid hexadecimal " + hex);
        return color(Integer.valueOf(hex.substring(1, 3), 16), Integer.valueOf(hex.substring(3, 5), 16), Integer.valueOf(hex.substring(5, 7), 16));
    }

    public LeatherArmorBuilder clearColor() {
        LeatherArmorMeta meta = getItemMeta();
        meta.setColor(null);
        item.setItemMeta(meta);
        return this;
    }
}
