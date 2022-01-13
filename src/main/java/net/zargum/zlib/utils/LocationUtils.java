package net.zargum.zlib.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class LocationUtils {

    public static String toStringNoYawPitch(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return "x: " + x + ", y: " + y + ", z: " + z;
    }

    public static String getLocationParsed(Location location) {
        return location.getBlockX() + "" + location.getBlockZ();
    }

    public static Location getFromConfig(FileConfiguration config, String path) {
        return new Location(
                Bukkit.getWorld(config.getString(path + ".world")),
                config.getDouble(path + ".x"),
                config.getDouble(path + ".y"),
                config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"),
                (float) config.getDouble(path + ".pitch")
        );
    }

    public static void toConfig(FileConfiguration config, String path, Location location) {
        config.set(path + ".world", location.getWorld().getName());
        config.set(path + ".x", location.getX());
        config.set(path + ".y", location.getY());
        config.set(path + ".z", location.getZ());
        config.set(path + ".yaw", location.getYaw());
        config.set(path + ".pitch", location.getPitch());
    }
}
