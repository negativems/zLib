package net.zargum.zlib.menu.impl;

import net.zargum.zlib.menu.button.Button;
import net.zargum.zlib.menu.button.ButtonMenu;
import net.zargum.zlib.messages.Messages;
import net.zargum.zlib.sounds.SoundUtil;
import net.zargum.zlib.sounds.Sounds;
import net.zargum.zlib.textures.Texture;
import net.zargum.zlib.utils.item.ItemBuilder;
import net.zargum.zlib.utils.item.SkullBuilder;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class AmountMenu extends ButtonMenu<zLib> {

    private final Consumer<Integer> consumer;
    private int amount;
    private final int maxAmount, leftClickAmount, middleClickAmount, rightClickAmount;

    public AmountMenu(Consumer<Integer> consumer, int defaultAmount, int maxAmount, int leftClickAmount, int middleClickAmount, int rightClickAmount) {
        super(ChatColor.YELLOW + "Select an amount", 3 * 9);
        this.consumer = consumer;
        this.amount = defaultAmount;
        this.maxAmount = maxAmount;
        this.leftClickAmount = leftClickAmount;
        this.middleClickAmount = middleClickAmount;
        this.rightClickAmount = rightClickAmount;
    }

    public AmountMenu(Consumer<Integer> consumer, int defaultAmount, int maxAmount) {
        this(consumer, defaultAmount, maxAmount, 1, 10, 100);
    }

    public AmountMenu(Consumer<Integer> consumer, int defaultAmount) {
        this(consumer, defaultAmount, 1000, 1, 10, 100);
    }

    public AmountMenu(Consumer<Integer> consumer) {
        this(consumer, 0, 1000, 1, 10, 100);
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        buttons.put(11, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new SkullBuilder().setTexture(Texture.RED_COLOR)
                        .setDisplayName(ChatColor.RED + "Decrease the amount")
                        .setLore(Messages.AMOUNT_MENU_LORE.toStringList("decrease", leftClickAmount + "", middleClickAmount + "", rightClickAmount + "", "from"))
                        .build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                click(player, type, false);
            }
        });

        updateAmount(); // To set amount item on the inventory

        buttons.put(15, new Button() {
            @Override
            public ItemStack getIcon(Player player) {
                return new SkullBuilder().setTexture(Texture.GREEN_COLOR)
                        .setDisplayName(ChatColor.GREEN + "Increase the amount")
                        .setLore(Messages.AMOUNT_MENU_LORE.toStringList("increase", leftClickAmount + "", middleClickAmount + "", rightClickAmount + "", "to"))
                        .build();
            }

            @Override
            public void onClick(Player player, int slot, ClickType type, InventoryAction action) {
                click(player, type, true);
            }
        });
        return buttons;
    }

    @Override
    public void onInventoryClose(InventoryCloseEvent event) {
        Inventory topInventory = event.getView().getTopInventory();
        if (topInventory.equals(inventory)) {
            consumer.accept(amount);
        }
    }

    private void updateAmount() {
        ItemStack amountItem = new ItemBuilder(Material.PAPER)
                .setDisplayName(ChatColor.GOLD + "Amount: " + ChatColor.YELLOW + amount)
                .setLore(ChatColor.DARK_GRAY + "" + ChatColor.ITALIC + "Maximum amount: " + ChatColor.GRAY + "" + ChatColor.ITALIC + "" + ChatColor.UNDERLINE + maxAmount)
                .build();
        inventory.setItem(13, amountItem);
        
    }

    private void click(Player player, ClickType clickType, boolean increase) {
        boolean leftClicked = clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT;
        boolean middleClicked = clickType == ClickType.MIDDLE;
        boolean rightClicked = clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT;
        int clickAmount = leftClicked ? leftClickAmount : middleClicked ? middleClickAmount : rightClicked ? rightClickAmount : leftClickAmount;

        if (increase) {
            if (amount + clickAmount > maxAmount) {
                SoundUtil.play(player, Sounds.ERROR);
                return;
            }
            amount = amount + clickAmount;
            updateAmount();
            SoundUtil.play(player, Sounds.CLICK);
            return;
        }

        if (amount - clickAmount < 0) {
            SoundUtil.play(player, Sounds.ERROR);
            return;
        }
        amount = amount - clickAmount;
        updateAmount();
        SoundUtil.play(player, Sounds.CLICK);
    }
}