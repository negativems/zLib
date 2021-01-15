package net.zargum.zlib.utils;

import com.google.common.base.Preconditions;
import net.minecraft.server.v1_8_R3.EnumItemRarity;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Map;

public final class InventoryUtils {

    public static final int DEFAULT_INVENTORY_WIDTH = 9;

    public static final int MINIMUM_INVENTORY_HEIGHT = 1;
    public static final int MINIMUM_INVENTORY_SIZE = MINIMUM_INVENTORY_HEIGHT * DEFAULT_INVENTORY_WIDTH;

    public static final int MAXIMUM_INVENTORY_HEIGHT = 6;
    public static final int MAXIMUM_INVENTORY_SIZE = MAXIMUM_INVENTORY_HEIGHT * DEFAULT_INVENTORY_WIDTH;

    public static final int MAXIMUM_SINGLE_CHEST_SIZE = DEFAULT_INVENTORY_WIDTH * 3;
    public static final int MAXIMUM_DOUBLE_CHEST_SIZE = MAXIMUM_SINGLE_CHEST_SIZE * 2;

    private InventoryUtils() {
    }

    public static ItemStack[] deepClone(ItemStack[] origin) {
        Preconditions.checkNotNull(origin, "Origin cannot be null");
        ItemStack[] cloned = new ItemStack[origin.length];
        for (int i = 0; i < origin.length; i++) {
            ItemStack next = origin[i];
            cloned[i] = next == null ? null : next.clone();
        }

        return cloned;
    }

    public static ItemStack[] getFixedOrder(ItemStack[] items) {
        ItemStack[] result = new ItemStack[36];
        System.arraycopy(items, 0, result, 27, 9);
        System.arraycopy(items, 9, result, 0, 27);
        return result;
    }

    public static int getSafestInventorySize(int initialSize) {
        return (initialSize + (DEFAULT_INVENTORY_WIDTH - 1)) / DEFAULT_INVENTORY_WIDTH * DEFAULT_INVENTORY_WIDTH;
    }

    public static int repairItem(ItemStack item) {
        if (item == null) return 0;

        Material material = Material.getMaterial(item.getTypeId());
        if (material.isBlock() || material.getMaxDurability() < 1) return 0;

        if (item.getDurability() <= 0) return 0;

        item.setDurability((short) 0);

        return 1;
    }

    public static void removeItem(Inventory inventory, Material type, short data, int quantity) {
        boolean compareDamage = type.getMaxDurability() == 0;

        for (int i = quantity; i > 0; i--) {
            for (ItemStack item : inventory.getContents()) {
                if (item == null || item.getType() != type) continue;
                if (compareDamage && item.getData().getData() != data) continue;

                if (item.getAmount() <= 1) {
                    inventory.removeItem(item);
                } else {
                    item.setAmount(item.getAmount() - 1);
                }
                break;
            }
        }
    }

    public static int countAmount(Inventory inventory, Material type, short data) {
        boolean compareDamage = type.getMaxDurability() == 0;

        int counter = 0;

        for (ItemStack item : inventory.getContents()) {
            if (item == null || item.getType() != type) continue;
            if (compareDamage && item.getData().getData() != data) continue;

            counter += item.getAmount();
        }

        return counter;
    }

    public static boolean isEmpty(Inventory inventory) {
        return isEmpty(inventory, true);
    }

    public static boolean isEmpty(Inventory inventory, boolean checkArmour) {
        boolean result = true;

        ItemStack[] contents = inventory.getContents();
        for (ItemStack content : contents) {
            if (content != null && content.getType() != Material.AIR) {
                result = false;
                break;
            }
        }

        if (!result) return false;

        if (checkArmour && inventory instanceof PlayerInventory) {
            contents = ((PlayerInventory) inventory).getArmorContents();
            for (ItemStack content : contents) {
                if (content != null && content.getType() != Material.AIR) {
                    result = false;
                    break;
                }
            }
        }

        return result;
    }

    public static boolean clickedTopInventory(InventoryDragEvent event) {
        InventoryView view = event.getView();
        Inventory inventory = view.getTopInventory();
        if (inventory == null) return false;

        boolean result = false;

        for (Map.Entry<Integer, ItemStack> entry : event.getNewItems().entrySet()) {
            if (entry.getKey() < inventory.getSize()) {
                result = true;
                break;
            }
        }

        return result;
    }

    private static char rarityAsChar(EnumItemRarity rarity) {
        return rarity == EnumItemRarity.COMMON ? 'f' : rarity == EnumItemRarity.UNCOMMON ? 'e' : rarity == EnumItemRarity.RARE ? 'b' : 'd';
    }

    public static String getName(ItemStack item) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        return ChatColor.getByChar(rarityAsChar(nmsItem.u())) + nmsItem.getName();
    }

    public static void clearInventory(Player player) {
        player.setItemOnCursor(null);

        Inventory topInventory = player.getOpenInventory().getTopInventory();
        if (topInventory != null && topInventory.getType() == InventoryType.CRAFTING) {
            topInventory.clear();
        }

        PlayerInventory inventory = player.getInventory();
        inventory.setArmorContents(null);
        inventory.clear();
    }
}