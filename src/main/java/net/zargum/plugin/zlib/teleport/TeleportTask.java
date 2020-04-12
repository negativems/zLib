package net.zargum.plugin.zlib.teleport;

import net.zargum.plugin.zlib.events.PlayerCooldownTeleport;
import net.zargum.plugin.zlib.events.TeleportCancelledEvent;
import net.zargum.plugin.zlib.utils.LocationUtil;
import net.zargum.plugin.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class TeleportTask extends BukkitRunnable {

    final private zLib plugin;
    private TeleportManager manager;

    public TeleportTask(zLib plugin) {
        this.plugin = plugin;
        manager = plugin.getTeleportManager();
    }

    @Override
    public void run() {
        for (UUID uniqueId : manager.getLocationMap().keySet()) {
            Player player = Bukkit.getPlayer(uniqueId);
            String parsedPlayerLocation = LocationUtil.getLocationParsed(player.getLocation());
            String parsedSavedLocation = LocationUtil.getLocationParsed(manager.getLocationMap().get(uniqueId));

            if (!parsedPlayerLocation.equals(parsedSavedLocation)) {
                TeleportCancelledEvent teleportCancelledEvent = new TeleportCancelledEvent(player, manager.getLocationMap().get(uniqueId)); // Initialize your Event
                Bukkit.getPluginManager().callEvent(teleportCancelledEvent); // This fires the event and allows any listener to listen to the event
                manager.getTimeMap().remove(uniqueId);
                manager.getCooldownMap().remove(uniqueId);
                manager.getLocationMap().remove(uniqueId);
                continue;
            }

            int cooldownTime = manager.getCooldownMap().get(uniqueId);
            if (manager.getTimePassedSinceStart(uniqueId) >= cooldownTime) {
                Location location = manager.getLocationMap().get(uniqueId);
                PlayerCooldownTeleport playerCooldownTeleport = new PlayerCooldownTeleport(player, location, cooldownTime); // Initialize your Event
                Bukkit.getPluginManager().callEvent(playerCooldownTeleport); // This fires the event and allows any listener to listen to the event
                if (!playerCooldownTeleport.isCancelled()) {
                    player.teleport(manager.getLocationMap().get(uniqueId));
                    manager.getLocationMap().remove(uniqueId);
                    manager.getTimeMap().remove(uniqueId);
                    manager.getCooldownMap().remove(uniqueId);
                }
            }
        }
    }
}
