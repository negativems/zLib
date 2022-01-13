package net.zargum.zlib.menu.impl;

import net.zargum.zlib.menu.button.Button;
import net.zargum.zlib.menu.button.ButtonMenu;
import net.zargum.zlib.utils.item.ItemBuilder;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ConfirmationMenu extends ButtonMenu<zLib> {

    private final Consumer<Result> consumer;
    private final String action;

    public ConfirmationMenu(Consumer<Result> consumer, String action) {
        super("Are you sure?", 3 * 9);

        this.consumer = consumer;
        this.action = action;
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(11, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new ItemBuilder(Material.STAINED_CLAY).setDurability(13).setDisplayName(ChatColor.GREEN + "Yes").addLore(ChatColor.YELLOW + "Click to " + action).build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                consumer.accept(Result.ACCEPT);
            }
        });

        buttons.put(15, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new ItemBuilder(Material.STAINED_CLAY).setDurability(14).setDisplayName(ChatColor.RED + "No").addLore(ChatColor.YELLOW + "Click to return to the previous menu.").build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                consumer.accept(Result.DENY);
                player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
            }
        });
        return buttons;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(inventory)) {
            consumer.accept(Result.CLOSE);
        }
    }

    public enum Result {
        ACCEPT, DENY, CLOSE
    }
}