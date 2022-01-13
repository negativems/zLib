package net.zargum.zlib.menu;

import com.google.common.base.Strings;
import net.zargum.zlib.textures.Texture;
import net.zargum.zlib.utils.InventoryUtils;
import net.zargum.zlib.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Menu<T extends JavaPlugin> implements InventoryHolder {

    public final T plugin = JavaPlugin.getPlugin((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

    public static final ItemStack CLOSE_ITEM = new ItemBuilder(Material.BARRIER).setDisplayName(ChatColor.RED + "Close").build();
    public static final ItemStack GO_BACK_ITEM = Texture.LEAVE_MENU.asSkullBuilder().setDisplayName(ChatColor.RED + "Go back").build();

    public static final String ITEM_LORE_STRAIGHT_LINE = ChatColor.STRIKETHROUGH + Strings.repeat("-", 33);

    public List<Integer> slotsOnTask = new ArrayList<>();

    public boolean isAutoUpdate() {
        return false;
    }

    public void update(Player player) {
    }

    public void open(Player player, boolean forceUpdate) {
        player.openInventory(getInventory());

        update(player);

        if (forceUpdate) {
            player.updateInventory();
        }
    }

    public void open(Player player) {
        open(player, true);
    }

    public void onInventoryClick(InventoryClickEvent event) {
    }

    public void onInventoryDrag(InventoryDragEvent event) {
        if (InventoryUtils.clickedTopInventory(event)) {
            event.setCancelled(true);
        }
    }

    public void onInventoryClose(InventoryCloseEvent event) {
    }

    public boolean isMenuOpen(Player player) {
        return player.getOpenInventory() != null && player.getOpenInventory().getTopInventory().getHolder() == getInventory().getHolder();
    }

    public void addTemporaryLore(Player player, int slot, String... lore) {
        slotsOnTask.add(slot);
        ItemStack item = getInventory().getItem(slot);
        ItemBuilder itemBuilder = new ItemBuilder(item);
        getInventory().setItem(slot, itemBuilder.addLore(lore).build());
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            if (isMenuOpen(player)) {
                List<String> cleanLore = item.getItemMeta().getLore().subList(0, item.getItemMeta().getLore().size() - lore.length);
                getInventory().setItem(slot, itemBuilder.setLore(cleanLore).build());
            }
            slotsOnTask.remove((Integer) slot);
        }, 20);
    }

    public void fill(ItemStack item, Integer... blankSlots) {
        List<Integer> blankSlotsList = Arrays.asList(blankSlots);
        for (int i = 0; i < getInventory().getSize(); i++) {
            ItemStack itemAtSlot = getInventory().getItem(i);
            if ((itemAtSlot == null || itemAtSlot.getType() == Material.AIR) && !blankSlotsList.contains(i)) {
                getInventory().setItem(i, item);
            }
        }
    }
}