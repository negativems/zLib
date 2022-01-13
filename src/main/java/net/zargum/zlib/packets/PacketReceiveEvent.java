package net.zargum.zlib.packets;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class PacketReceiveEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final Packet<?> packet;
    private final Player player;

    public PacketReceiveEvent(Packet<?> packet, Player player) {
        this.packet = packet;
        this.player = player;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
