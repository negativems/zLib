package net.zargum.zlib.events;

import lombok.Getter;
import lombok.Setter;
import net.zargum.zlib.teleport.TeleportMap;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TeleportCancelledEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter @Setter private Location locationFrom;
    @Getter @Setter private Location locationTo;
    @Getter @Setter private boolean isCancelled;

    public TeleportCancelledEvent(Player player, TeleportMap teleportMap) {
        this.player = player;
        this.locationFrom = teleportMap.getFrom();
        this.locationTo = teleportMap.getTo();
        this.isCancelled = false;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}