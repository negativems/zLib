package net.zargum.zlib.npc;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InteractNPCEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter private final NPC npc;
    @Getter @Setter private boolean cancelled;

    public InteractNPCEvent(Player player, NPC npc) {
        this.player = player;
        this.npc = npc;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}