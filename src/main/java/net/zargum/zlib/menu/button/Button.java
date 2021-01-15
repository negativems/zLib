package net.zargum.zlib.menu.button;

import net.zargum.zlib.menu.button.impl.BackButton;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public interface Button {

    BackButton BACK_BUTTON = new BackButton();

    ItemStack getIcon(Player player);

    default void onClick(Player player, int slot, ClickType type, InventoryAction action) {
    }

    default void onClick(InventoryClickEvent event) {
        onClick((Player) event.getWhoClicked(), event.getRawSlot(), event.getClick(), event.getAction());
    }
}