package net.zargum.zlib.menu;

import com.google.common.base.Strings;
import net.zargum.zlib.utils.InventoryUtils;
import net.zargum.zlib.utils.Texture;
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

public abstract class Menu<T extends JavaPlugin> implements InventoryHolder {

    public final T plugin = JavaPlugin.getPlugin((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);

    public static final ItemStack CLOSE_ITEM = new ItemBuilder(Material.BARRIER).setName(ChatColor.RED + "Close").build();
    public static final ItemStack GO_BACK_ITEM = Texture.LEAVE_MENU.asSkullBuilder().setName(ChatColor.RED + "Go back").build();

    public static final String ITEM_LORE_STRAIGHT_LINE = ChatColor.STRIKETHROUGH + Strings.repeat("-", 33);

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
}