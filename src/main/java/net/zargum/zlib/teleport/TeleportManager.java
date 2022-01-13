package net.zargum.zlib.teleport;

import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.UUID;

public class TeleportManager {

    final private zLib plugin;
    @Getter private final HashMap<UUID, TeleportMap> locationMap = new HashMap<>(); // Location of the player
    @Getter private final HashMap<UUID, Long> timeMap = new HashMap<>(); // Time since the start
    @Getter private final HashMap<UUID, Integer> cooldownMap = new HashMap<>(); // How much seconds of the cooldown

    public TeleportManager(zLib plugin) {
        this.plugin = plugin;
    }

    public void addPlayer(UUID uniqueId, int cooldownTime, Location locationTo) {
        timeMap.put(uniqueId, System.currentTimeMillis());
        Location locationFrom = plugin.getServer().getPlayer(uniqueId).getLocation();
        locationMap.put(uniqueId, new TeleportMap(locationFrom, locationTo));
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
