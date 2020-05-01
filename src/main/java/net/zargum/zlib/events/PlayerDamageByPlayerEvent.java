package net.zargum.zlib.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDamageByPlayerEvent extends Event implements Cancellable {

    @Getter private final Player damager;
    @Getter private final Player victim;
    @Getter private final double damage;
    private boolean isCancelled;

    public PlayerDamageByPlayerEvent(Player damager, Player victim, Double damage) {
        this.damager = damager;
        this.victim = victim;
        this.damage = damage;
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