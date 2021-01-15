package net.zargum.zlib.menu.deprecated;

import lombok.Getter;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.function.Consumer;
@Deprecated
public class MenuButton {

    @Getter private HashMap<ClickType, Consumer<InventoryClickEvent>> events;

    public MenuButton() {
        this.events = new HashMap<>();
    }

    public boolean containsEvent(ClickType clickType) {
        return events.containsKey(clickType);
    }

     public void onLeftClick(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.LEFT, event);
     }
     public void onRightClick(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.RIGHT, event);
     }
     public void onShiftLeftClick(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.SHIFT_LEFT, event);
     }
     public void onShiftRightClick(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.SHIFT_RIGHT, event);
     }
     public void onMiddleClick(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.MIDDLE, event);
     }
     public void onDrop(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.DROP, event);
     }
     public void onControlDrop(Consumer<InventoryClickEvent> event) {
         events.put(ClickType.CONTROL_DROP, event);
     }

}
