package net.zargum.zlib.menu;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class MenuListener implements Listener {

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof MenuBuilder)) return;

        MenuBuilder menu = (MenuBuilder) holder;
        Consumer<InventoryOpenEvent> openListener = menu.getOpenListener();

        if (openListener != null) openListener.accept(event);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        InventoryHolder holder = event.getInventory().getHolder();
        if (!(holder instanceof MenuBuilder)) return;

        MenuBuilder menu = (MenuBuilder) holder;
        Consumer<InventoryCloseEvent> closeListener = menu.getCloseListener();

        if (closeListener != null) closeListener.accept(event);
    }

    @EventHandler
    public void onPickup(InventoryPickupItemEvent event) { // When hopper pickup an item
        InventoryHolder holder = event.getInventory().getHolder();

        if (!(holder instanceof MenuBuilder)) return;

        MenuBuilder menu = (MenuBuilder) holder;
        Consumer<InventoryPickupItemEvent> pickupListener = menu.getPickupListener();

        if (pickupListener != null) pickupListener.accept(event);
    }

    @EventHandler
    public void listenClick(InventoryClickEvent event) {

        Inventory inventory = event.getClickedInventory();
        if (inventory == null) return;

        InventoryHolder holder = inventory.getHolder();
        if (!(holder instanceof MenuBuilder)) return;

        final ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        MenuBuilder menu = (MenuBuilder) holder;
        MenuButton button = menu.getButtonBySlot(event.getSlot());

        if (button != null && button.containsEvent(event.getClick())) {
            if (!menu.getPickableItems().contains(clickedItem)) event.setCancelled(true);
            Consumer clickedEvent = button.getEvents().get(event.getClick());
            clickedEvent.accept(event);
        } else {
            if (menu.isCancelledClickToOtherItems()) event.setCancelled(true);
        }
    }
}
