package net.zargum.zlib.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class PlayerFirstJoinEvent extends PlayerSpawnLocationEvent implements Listener {

    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    @Getter private final Location spawnLocation;

    public PlayerFirstJoinEvent(Player player, Location spawnLocation) {
        super(player, spawnLocation);
        this.player = player;
        this.spawnLocation = spawnLocation;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @EventHandler
    public void onFirstJoin(PlayerSpawnLocationEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            PlayerFirstJoinEvent playerFirstJoinEvent = new PlayerFirstJoinEvent(event.getPlayer(), event.getSpawnLocation());
            Bukkit.getPluginManager().callEvent(playerFirstJoinEvent);
        }
    }
}
