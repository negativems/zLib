package net.zargum.zlib.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StaffUtil {

    public static void messageToHighStaff(String message) {
        message = ColorUtils.translate(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("staff.high")) player.sendMessage(message);
        }
    }

    public static void messageToHighStaff(String message, Player sender) {
        message = ColorUtils.translate(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == sender) continue;
            if (player.hasPermission("staff.high")) player.sendMessage(message);
        }
    }

    public static void message(String message, Player sender) {
        message = ColorUtils.translate(message);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sender != null && sender == player) continue;
            if (player.hasPermission("staff")) player.sendMessage(message);
        }
    }
    public static void message(String message) {
        message(message, null);
    }


    public static void log(String message, Player sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (sender != null && sender != player) continue;
            if (player.hasPermission("staff.logs")) {
                player.sendMessage(ChatColor.DARK_PURPLE + "# " + ChatColor.DARK_GRAY + "[" + ChatColor.DARK_PURPLE + "LOG" + ChatColor.DARK_GRAY + "] " + ChatColor.RESET + message);
            }
        }
    }
    public static void log(String message) {
        log(message, null);
    }

    public static void playSound(Sound sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("staff")) player.playSound(player.getLocation(), sound, 10F, 2F);
        }
    }

    public static void playSound(Sound sound, Player sender) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player == sender) continue;
            if (player.hasPermission("staff")) player.playSound(player.getLocation(), sound, 10F, 2F);
        }
    }

    public static int getStaffCount() {
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) if (player.hasPermission("staff")) count++;
        return count;
    }


}
