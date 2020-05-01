package net.zargum.zlib.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] translate(String[] list) {
        List<String> stringList = new ArrayList<>();
        for (String s : list) {
            stringList.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        return stringList.toArray(new String[0]);
    }

}
