package net.zargum.zlib.menu;

import net.zargum.zlib.zLib;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public final class MenuListener implements Listener {

    public MenuListener(zLib plugin) {
        new MenuUpdaterTask().runTaskTimerAsynchronously(plugin, 20L, 20L);
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (event.getWhoClicked() instanceof Player && holder instanceof Menu) {
            ((Menu<?>) holder).onInventoryClick(event);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (holder instanceof Menu) {
            ((Menu<?>) holder).onInventoryDrag(event);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (event.getPlayer() instanceof Player && holder instanceof Menu) {
            ((Menu<?>) holder).onInventoryClose(event);
        }
    }
}