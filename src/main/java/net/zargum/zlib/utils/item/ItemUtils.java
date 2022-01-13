package net.zargum.zlib.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemUtils {

    static Material[] TOOLS = new Material[]{
            Material.WOOD_SWORD, Material.WOOD_PICKAXE, Material.WOOD_SPADE, Material.WOOD_AXE,
            Material.WOOD_HOE, Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_SPADE,
            Material.STONE_AXE, Material.STONE_HOE, Material.IRON_SWORD, Material.IRON_PICKAXE,
            Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, Material.GOLD_SWORD,
            Material.GOLD_PICKAXE, Material.GOLD_SPADE, Material.GOLD_AXE, Material.GOLD_HOE,
            Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE,
            Material.DIAMOND_AXE, Material.DIAMOND_HOE
    };
    static Material[] OTHER_TOOLS = new Material[]{
            Material.FISHING_ROD, Material.COMPASS, Material.WATCH, Material.SHEARS, Material.FLINT_AND_STEEL,
            Material.LEASH
    };
    static Material[] PICKAXES = new Material[]{
            Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE,
            Material.DIAMOND_PICKAXE
    };
    static Material[] ARMOR = new Material[]{
            Material.CHAINMAIL_HELMET, Material.DIAMOND_HELMET, Material.GOLD_HELMET, Material.IRON_HELMET, Material.LEATHER_HELMET,
            Material.CHAINMAIL_CHESTPLATE, Material.DIAMOND_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.IRON_CHESTPLATE, Material.LEATHER_CHESTPLATE,
            Material.CHAINMAIL_LEGGINGS, Material.DIAMOND_LEGGINGS, Material.GOLD_LEGGINGS, Material.IRON_LEGGINGS, Material.LEATHER_LEGGINGS,
            Material.CHAINMAIL_BOOTS, Material.DIAMOND_BOOTS, Material.GOLD_BOOTS, Material.IRON_BOOTS, Material.LEATHER_BOOTS,
    };

    public static boolean isPickaxe(Material material) {
        return material != null && Arrays.asList(PICKAXES).contains(material);
    }

    public static boolean isPickaxe(ItemStack item) {
        return item != null && Arrays.asList(PICKAXES).contains(item.getType());
    }

    public static boolean isArmor(ItemStack item) {
        return item != null && Arrays.asList(ARMOR).contains(item.getType());
   }

}
