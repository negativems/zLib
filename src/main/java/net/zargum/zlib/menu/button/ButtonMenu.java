package net.zargum.zlib.menu.button;

import lombok.Getter;
import net.zargum.zlib.menu.type.ChestMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

@Getter
public abstract class ButtonMenu<T extends JavaPlugin> extends ChestMenu<T> {

    protected Map<Integer, Button> buttons;

    public ButtonMenu(String title, int size) {
        super(title, size);
    }

    public ButtonMenu(String title) {
        super(title, 3 * 9);
    }

    public abstract Map<Integer, Button> getButtons(Player player);

    @Override
    public void update(Player player) {
        (buttons = getButtons(player)).forEach((slot, button) -> inventory.setItem(slot, button.getIcon(player)));
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (!topInventory.equals(inventory)) return;

        Inventory clickedInventory = event.getClickedInventory();
        if (topInventory.equals(clickedInventory)) {
            event.setCancelled(true);

            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR || !item.hasItemMeta()) return;

            Button button = buttons.get(event.getRawSlot());
            if (button != null) {
                button.onClick(event);
            }
        } else if (!topInventory.equals(clickedInventory) && (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY || event.getAction() == InventoryAction.COLLECT_TO_CURSOR)) {
            event.setCancelled(true);
        }
    }
}