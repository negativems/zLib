package net.zargum.zlib.teleport;

import net.zargum.zlib.events.PlayerCooldownTeleport;
import net.zargum.zlib.events.TeleportCancelledEvent;
import net.zargum.zlib.utils.LocationUtils;
import net.zargum.zlib.zLib;
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
            String parsedPlayerLocation = LocationUtils.getLocationParsed(player.getLocation());
            String parsedSavedLocation = LocationUtils.getLocationParsed(manager.getLocationMap().get(uniqueId).getFrom());

            if (!parsedPlayerLocation.equals(parsedSavedLocation)) {
                TeleportCancelledEvent teleportCancelledEvent = new TeleportCancelledEvent(player, manager.getLocationMap().get(uniqueId));
                Bukkit.getPluginManager().callEvent(teleportCancelledEvent);
                manager.getTimeMap().remove(uniqueId);
                manager.getCooldownMap().remove(uniqueId);
                manager.getLocationMap().remove(uniqueId);
                continue;
            }

            int cooldownTime = manager.getCooldownMap().get(uniqueId);
            if (manager.getTimePassedSinceStart(uniqueId) >= cooldownTime) {
                Location toLocation = manager.getLocationMap().get(uniqueId).getTo();
                PlayerCooldownTeleport playerCooldownTeleport = new PlayerCooldownTeleport(player, toLocation, cooldownTime);
                Bukkit.getPluginManager().callEvent(playerCooldownTeleport);
                if (!playerCooldownTeleport.isCancelled()) {
                    player.teleport(toLocation);
                    manager.getLocationMap().remove(uniqueId);
                    manager.getTimeMap().remove(uniqueId);
                    manager.getCooldownMap().remove(uniqueId);
                }
            }
        }
    }
}
