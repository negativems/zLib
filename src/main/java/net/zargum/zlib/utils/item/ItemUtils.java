package net.zargum.zlib.utils.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemUtils {

    static Material[] TOOLS = new Material[] {Material.WOOD_SWORD, Material.WOOD_PICKAXE, Material.WOOD_SPADE, Material.WOOD_AXE, Material.WOOD_HOE, Material.STONE_SWORD, Material.STONE_PICKAXE, Material.STONE_SPADE, Material.STONE_AXE, Material.STONE_HOE, Material.IRON_SWORD, Material.IRON_PICKAXE, Material.IRON_SPADE, Material.IRON_AXE, Material.IRON_HOE, Material.GOLD_SWORD, Material.GOLD_PICKAXE, Material.GOLD_SPADE, Material.GOLD_AXE, Material.GOLD_HOE, Material.DIAMOND_SWORD, Material.DIAMOND_PICKAXE, Material.DIAMOND_SPADE, Material.DIAMOND_AXE, Material.DIAMOND_HOE };
    static Material[] OTHER_TOOLS = new Material[] {Material.FISHING_ROD, Material.COMPASS, Material.WATCH, Material.SHEARS, Material.FLINT_AND_STEEL, Material.LEASH};
    static Material[] PICKAXES = new Material[] {Material.WOOD_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLD_PICKAXE, Material.DIAMOND_PICKAXE};

    public static boolean isPickaxe(Material material) {
        return material != null && Arrays.asList(PICKAXES).contains(material);
    }

    public static boolean isPickaxe(ItemStack item) {
        return item != null && Arrays.asList(PICKAXES).contains(item.getType());
    }

}
