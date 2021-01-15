package net.zargum.zlib.menu.impl;

import net.zargum.zlib.menu.Menu;
import net.zargum.zlib.menu.button.Button;
import net.zargum.zlib.menu.button.ButtonMenu;
import net.zargum.zlib.menu.button.impl.BackButton;
import net.zargum.zlib.utils.BukkitUtils;
import net.zargum.zlib.utils.item.ItemBuilder;
import net.zargum.zlib.zLib;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class ColorSelectorMenu extends ButtonMenu<zLib> {

    private final Menu menu;
    private final Consumer<ChatColor> consumer;

    public ColorSelectorMenu(Menu<JavaPlugin> menu, Consumer<ChatColor> consumer) {
        super("Choose a color", 5 * 9);

        this.menu = menu;
        this.consumer = consumer;
    }

    private void chooseColor(Player player, ChatColor color) {
        if (menu != null) {
            menu.open(player);
        } else {
            player.closeInventory();
        }

        consumer.accept(color);

        player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 1.0f);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        buttons.put(0, new BackButton(menu));

        int slot = 10;

        for (ChatColor color : BukkitUtils.SORTED_COLORS) {
            buttons.put(slot++, new Button() {
                @Override
                public ItemStack getIcon(Player player) {
                    ItemBuilder builder = new ItemBuilder(BukkitUtils.toHeadSkull(color).clone());

                    String name = WordUtils.capitalizeFully((color.name()).replace("_", " "));
                    builder.setName(color + name);
                    builder.addLoreLine(ChatColor.GRAY + ITEM_LORE_STRAIGHT_LINE);
                    builder.addLoreLine(ChatColor.YELLOW + "Click to select the " + color + name + ChatColor.YELLOW + " color.");
                    builder.addLoreLine(ChatColor.GRAY + ITEM_LORE_STRAIGHT_LINE);
                    return builder.build();
                }

                @Override
                public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                    chooseColor(player, color);
                }
            });

            if ((slot - 8) % 9 == 0) {
                slot += 2;
            }
        }

        buttons.put(40, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new ItemBuilder(Material.BARRIER)
                        .setName(ChatColor.WHITE + "Reset Color")
                        .addLoreLine(ChatColor.GRAY + ITEM_LORE_STRAIGHT_LINE)
                        .addLoreLine(ChatColor.YELLOW + "Click to reset your current color.")
                        .addLoreLine(ChatColor.GRAY + ITEM_LORE_STRAIGHT_LINE)
                        .build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                chooseColor(player, ChatColor.RESET);
            }
        });

        return buttons;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(inventory)) {
            consumer.accept(null);
        }
    }
}