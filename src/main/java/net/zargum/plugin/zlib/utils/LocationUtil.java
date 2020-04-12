package net.zargum.plugin.zlib.utils;

import org.bukkit.Location;

public class LocationUtil {

    public static String toStringNoYawPitch(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        return "x: " + x + ", y: " + y + ", z: " + z;
    }

    public static String getLocationParsed(Location location) {
        return location.getBlockX()+""+location.getBlockZ();
    }

}
