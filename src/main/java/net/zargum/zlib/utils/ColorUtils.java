package net.zargum.zlib.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class ColorUtils {

    public static String unformat(String s) {
        if (s == null) return "";
        return s.replaceAll(String.valueOf(ChatColor.COLOR_CHAR), "&");
    }

    public static String translate(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] translate(String[] list) {
        List<String> listString = new ArrayList<>();
        for (String line : list) {
            listString.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return listString.toArray(new String[0]);
    }

    public static List<String> translate(List<String> list) {
        List<String> listString = new ArrayList<>();
        for (String line : list) {
            listString.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        return listString;
    }

    // Max 81
    public static String getColoredEmptyTextById(int n) {
        if (n < 1 || n > 150) throw new IllegalStateException("Number < 1");
        String result = "";
        int x = 0;
        int y = 0;
        for (ChatColor color : ChatColor.values()) {
            x++;
            for (ChatColor color2 : ChatColor.values()) {
                y++;

            }
        }
        return result;
    }

    public static ChatColor getLastColor(String s) {
        String lastColors = ChatColor.getLastColors(s);
        if (lastColors.length() == 0) return null;
        for (int i = lastColors.length(); i > 0; i = i-2) {
            char colorCode = lastColors.charAt(i - 1);
            if (ChatColor.getByChar(colorCode).isColor() || ChatColor.getByChar(colorCode) == ChatColor.RESET) {
                return ChatColor.getByChar(colorCode);
            }
        }
        return null;
    }

    public static ChatColor getLastFormat(String s) {
        String lastColors = ChatColor.getLastColors(s);
        if (lastColors.length() == 0) return null;
        for (int i = lastColors.length(); i > 0; i = i-2) {
            char colorCode = lastColors.charAt(i - 1);
            if (ChatColor.getByChar(colorCode).isFormat()) {
                return ChatColor.getByChar(colorCode);
            }
            if (ChatColor.getByChar(colorCode).isColor()) {
                return null;
            }
        }
        return null;
    }

}
