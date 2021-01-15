package net.zargum.zlib.menu.button.impl;

import lombok.AllArgsConstructor;
import net.zargum.zlib.menu.Menu;
import net.zargum.zlib.menu.button.Button;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

import static net.zargum.zlib.menu.Menu.CLOSE_ITEM;
import static net.zargum.zlib.menu.Menu.GO_BACK_ITEM;

@AllArgsConstructor
public final class BackButton implements Button {

    private final Menu menu;

    public BackButton() {
        this(null);
    }

    @Override
    public ItemStack getIcon(Player player) {
        return menu != null ? GO_BACK_ITEM : CLOSE_ITEM;
    }

    @Override
    public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
        if (menu != null) {
            menu.open(player);
        } else {
            player.closeInventory();
        }

        player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
    }
}