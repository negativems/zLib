package net.zargum.zlib.menu;

import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Getter
public class MenuPattern {

    private List<String> pattern;
    private HashMap<Character, ItemStack> keys;

    public MenuPattern(List<String> pattern) {
        this.pattern = pattern;
        this.keys = new HashMap<>();
    }
    public MenuPattern(String... pattern) {
        this(Arrays.asList(pattern));
    }

    public MenuPattern setItem(Character c, ItemStack i) {
        this.keys.put(c,i);
        return this;
    }

}
