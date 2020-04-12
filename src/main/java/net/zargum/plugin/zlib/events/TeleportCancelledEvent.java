package net.zargum.plugin.zlib.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeleportCancelledEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter @Setter private final Location teleportTo;
    private boolean isCancelled;

    public TeleportCancelledEvent(Player player, Location location) {
        this.player = player;
        this.teleportTo = location;
        this.isCancelled = false;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}