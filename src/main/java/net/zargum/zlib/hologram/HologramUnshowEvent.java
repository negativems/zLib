package net.zargum.zlib.hologram;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.zargum.zlib.utils.Reflections;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HologramUnshowEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter private final Player player;
    @Getter private final Hologram hologram;
    @Getter @Setter private boolean cancelled;

    public HologramUnshowEvent(Player player, Hologram hologram) {
        this.player = player;
        this.hologram = hologram;
        for (EntityArmorStand stand : hologram.getEntities()) {
            if (stand == null) continue;
            Reflections.sendPacket(new PacketPlayOutEntityDestroy(stand.getId()), player);
        }
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }
    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}