package net.zargum.zlib.menu.type;

import lombok.Getter;
import lombok.Setter;
import net.zargum.zlib.menu.Menu;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class PaginatedMenu<T extends JavaPlugin> extends Menu<T> {

    @Getter
    @Setter
    protected int page = 1;

    protected Inventory inventory;

    public abstract int getTotalPages();

    public abstract int getSize();

    public abstract String getTitle();

    @Override
    public Inventory getInventory() {
        return inventory = plugin.getServer().createInventory(this, getSize(), getTitle());
    }

    @Override
    public abstract void update(Player player);
}