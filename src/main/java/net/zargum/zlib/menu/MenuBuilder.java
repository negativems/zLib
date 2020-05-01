package net.zargum.zlib.menu;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Getter
public class MenuBuilder implements InventoryHolder {

    private String title;
    private HashMap<Integer, ItemStack> items = new HashMap<>();
    private HashMap<ItemStack, MenuButton> buttons = new HashMap<>();
    private Inventory inventory;
    private boolean cancelledClickToOtherItems;
    private List<ItemStack> pickableItems = new ArrayList<>();
    @Setter private int rows;
    @Setter private MenuPattern pattern;
    private InventoryType inventoryType;
    @Setter private Consumer<InventoryOpenEvent> openListener;
    @Setter private Consumer<InventoryCloseEvent> closeListener;
    @Setter private Consumer<InventoryPickupItemEvent> pickupListener; // When a hopper or hopper minecart picks up a dropped item.

    public MenuBuilder(String title, int rows, InventoryType inventoryType) {
        this.title = title;
        this.rows = rows;
        this.inventoryType = inventoryType;
    }

    public void setTitle(String title) {
        this.title = ChatColor.translateAlternateColorCodes('&', title);
    }

    public void setCancelledClickToOtherItems(){
        this.cancelledClickToOtherItems = true;
    }

    public boolean isSlotEmpty(int i) {
        if (slotExist(i)) return !items.containsKey(i);
        throw new IllegalArgumentException("Invalid slot");
    }

    public ItemStack getItemAtSlot(int i) {
        if (!isSlotEmpty(i)) return null;
        return inventory.getItem(i);
    }

    public void setInventoryType(InventoryType inventoryType) {
        this.inventoryType = inventoryType;
        if (inventoryType.equals(InventoryType.CHEST)) inventory = Bukkit.createInventory(null, rows*9, title);
        inventory = Bukkit.createInventory(this, inventoryType, title);
    }

    public void setItem(int slot, ItemStack item) {
        if (!slotExist(slot)) throw new IllegalStateException("Error creating menu: tried to put an item in an invalid slot.");
        items.put(slot, item);
    }

    private void build() {
        if (pattern != null) {
            rows = pattern.getPattern().size();
            if (inventoryType.equals(InventoryType.CHEST)) inventory = Bukkit.createInventory(this, rows * 9, title);
            else inventory = Bukkit.createInventory(this, inventoryType, title);
            int slot = 0;
            for (String p : pattern.getPattern()) {
                if (!inventoryType.equals(InventoryType.CHEST) && p.length() != inventoryType.getDefaultSize()) {
                    String message = "Error creating menu ( " + inventoryType.getDefaultTitle() + " ): a pattern not contains " + inventoryType.getDefaultSize() + " chars/slots";
                    throw new IllegalStateException(message);
                }
                for (Character c : p.toCharArray()) {
                    if (!pattern.getKeys().containsKey(c)) throw new IllegalStateException("Error creating menu: pattern has char with no item defined.");
                    ItemStack item = pattern.getKeys().get(c);
                    if (!items.containsKey(slot)) this.items.put(slot, item);
                    slot++;
                }
            }
        }
        if (pattern == null && inventory == null) {
            if (inventoryType.equals(InventoryType.CHEST)) inventory = Bukkit.createInventory(this, rows * 9, title);
            else inventory = Bukkit.createInventory(this, inventoryType, title);
        }
        inventory.clear();
        for (Integer i : items.keySet()) {
            inventory.setItem(i, items.get(i));
        }
    }

    public void setButton(ItemStack item, ClickType clickType, Consumer<InventoryClickEvent> event) {
        setButton(item,clickType,event,true);
    }
    public void setButton(ItemStack item, ClickType clickType, Consumer<InventoryClickEvent> event, boolean cancel) {
        MenuButton button = new MenuButton();
        button.getEvents().put(clickType,event);
        buttons.put(item, button);
        if (!cancel) pickableItems.add(item);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void open(Player player) {
        build();
        player.openInventory(inventory);
    }

    // Utils
    private boolean slotExist(int slot) {
        return slot <= rows * getMaxSlotPerLine(inventoryType);
    }
    private int getMaxSlotPerLine(InventoryType inventoryType) {
        if (inventoryType.equals(InventoryType.CHEST)) return 9;
        return inventoryType.getDefaultSize();
    }
    public MenuButton getButtonByItem(ItemStack item) {
        return this.buttons.get(item);
    }
    public MenuButton getButtonBySlot(int slot) {
        ItemStack item = inventory.getItem(slot);
        return this.buttons.get(item);
    }

}
