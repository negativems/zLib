package net.zargum.plugin.zlib.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerCooldownTeleport extends Event implements Cancellable {

    @Getter private final Player player;
    @Getter private final int cooldownTime;
    @Getter @Setter private final Location teleportTo;
    private boolean isCancelled;

    public PlayerCooldownTeleport(Player player, Location location, int cooldownTime) {
        this.player = player;
        this.teleportTo = location;
        this.cooldownTime = cooldownTime;
        this.isCancelled = false;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}