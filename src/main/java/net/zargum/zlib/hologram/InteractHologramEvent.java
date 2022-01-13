package net.zargum.zlib.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InteractHologramEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter private final Hologram hologram;
    @Getter @Setter private boolean cancelled;

    public InteractHologramEvent(Player player, Hologram hologram) {
        this.player = player;
        this.hologram = hologram;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}