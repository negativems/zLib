package net.zargum.plugin.zlib.teleport;

import lombok.Getter;
import net.zargum.plugin.zlib.zLib;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class TeleportManager {

    final private zLib plugin;
    @Getter private HashMap<UUID, Location> locationMap = new HashMap<>(); // Location of the player
    @Getter private HashMap<UUID, Long> timeMap = new HashMap<>(); // Time since the start
    @Getter private HashMap<UUID, Integer> cooldownMap = new HashMap<>(); // How much seconds of the cooldown

    public TeleportManager(zLib plugin) {
        this.plugin = plugin;
    }

    public void addPlayer(UUID uniqueId, int cooldownTime, Location location) {
        timeMap.put(uniqueId, System.currentTimeMillis());
        locationMap.put(uniqueId, location);
        cooldownMap.put(uniqueId, cooldownTime);
    }

    public boolean existsPlayer(UUID uniqueId) {
        return locationMap.containsKey(uniqueId);
    }

    public long getTimePassedSinceStart(UUID uniqueId) {
        if (!existsPlayer(uniqueId)) throw new IllegalStateException("Player not exists in the cooldown list");
        long time = timeMap.get(uniqueId);
        return (System.currentTimeMillis() - time)/1000;
    }
}
