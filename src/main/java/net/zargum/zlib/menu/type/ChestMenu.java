package net.zargum.zlib.menu.type;

import lombok.Getter;
import net.zargum.zlib.menu.Menu;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public abstract class ChestMenu<T extends JavaPlugin> extends Menu<T> {

    protected final Inventory inventory;

    public ChestMenu(String title, int size) {
        inventory = plugin.getServer().createInventory(this, size, title.length() > 32 ? title.substring(0, 32) : title);
    }
}