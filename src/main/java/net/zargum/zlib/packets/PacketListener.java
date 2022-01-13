package net.zargum.zlib.packets;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PacketListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PacketReader packetReader = new PacketReader(event.getPlayer());
        packetReader.inject();
    }

}
