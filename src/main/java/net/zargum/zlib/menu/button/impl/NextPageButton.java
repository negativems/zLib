package net.zargum.zlib.menu.button.impl;

import lombok.AllArgsConstructor;
import net.zargum.zlib.menu.button.Button;
import net.zargum.zlib.menu.type.PaginatedMenu;
import net.zargum.zlib.utils.item.ItemBuilder;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public final class NextPageButton implements Button {

    private static final ItemStack DISALLOWED = new ItemBuilder(Material.CARPET).setName(ChatColor.GRAY + "Next Page").durability(7).build();
    private static final ItemStack ALLOWED = new ItemBuilder(Material.CARPET).setName(ChatColor.GREEN + "Next Page").durability(13).build();

    private final PaginatedMenu menu;

    @Override
    public ItemStack getIcon(Player player) {
        return menu.getPage() + 1 > menu.getTotalPages() ? DISALLOWED : ALLOWED;
    }

    @Override
    public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
        int page = menu.getPage();
        if (page + 1 > menu.getTotalPages()) return;

        menu.setPage(page + 1);
        menu.open(player, false);
    }
}