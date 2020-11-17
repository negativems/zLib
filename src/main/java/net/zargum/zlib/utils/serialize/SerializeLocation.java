package net.zargum.zlib.utils.serialize;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class SerializeLocation {

    public static JsonObject locationToJson(Location location) {
        if (location == null) return null;
        JsonObject values = new JsonObject();
        values.addProperty("x", location.getX());
        values.addProperty("y", location.getY());
        values.addProperty("z", location.getZ());
        values.addProperty("yaw", location.getYaw());
        values.addProperty("pitch", location.getPitch());
        values.addProperty("world", location.getWorld().getName());
        return values;
    }

    public static Location jsonToLocation(String string) {
        return jsonToLocation(new JsonParser().parse(string).getAsJsonObject());
    }

    public static Location jsonToLocation(JsonObject values) {
        if (Bukkit.getWorld(values.get("world").getAsString()) == null) return null;

        World world = Bukkit.getWorld(values.get("world").getAsString());
        double x = values.get("x").getAsDouble();
        double y = values.get("y").getAsDouble();
        double z = values.get("z").getAsDouble();
        float yaw = values.get("x").getAsFloat();
        float pitch = values.get("x").getAsFloat();

        return new Location(world, x, y, z, yaw, pitch);
    }

}
