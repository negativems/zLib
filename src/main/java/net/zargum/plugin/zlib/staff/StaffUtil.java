package net.zargum.plugin.zlib.staff;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class StaffUtil {

    public static void messageToHighStaff(String message){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.hasPermission("icarus.staff.high")) player.sendMessage(message);
        }
    }

    public static void message(String message){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.hasPermission("icarus.staff")) player.sendMessage(message);
        }
    }

    public static void playSound(Sound sound){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (player.hasPermission("icarus.staff")) player.playSound(player.getLocation(), sound, 10F, 2F);
        }
    }

    public static int getStaffCount(){
        int count = 0;
        for (Player player : Bukkit.getOnlinePlayers()) if (player.hasPermission("icarus.staff")) count++;
        return count;
    }


}
