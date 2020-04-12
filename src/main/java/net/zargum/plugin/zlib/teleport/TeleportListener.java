package net.zargum.plugin.zlib.teleport;

import net.zargum.plugin.zlib.zLib;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class TeleportListener implements Listener {

    final private zLib plugin;

    public TeleportListener(zLib plugin) {
        this.plugin = plugin;
    }

    public void onQuit(PlayerQuitEvent event) {
        plugin.getTeleportManager().getLocationMap().remove(event.getPlayer().getUniqueId());
        plugin.getTeleportManager().getTimeMap().remove(event.getPlayer().getUniqueId());
        plugin.getTeleportManager().getCooldownMap().remove(event.getPlayer().getUniqueId());
    }
}
