package net.zargum.zlib.menu;

import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.scheduler.BukkitRunnable;

@AllArgsConstructor
public final class MenuUpdaterTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            InventoryView view = player.getOpenInventory();
            if (view == null) continue;

            InventoryHolder holder = view.getTopInventory().getHolder();
            if (holder instanceof Menu) {
                Menu<?> menu = (Menu<?>) holder;
                if (menu.isAutoUpdate()) menu.update(player);
            }
        }
    }
}