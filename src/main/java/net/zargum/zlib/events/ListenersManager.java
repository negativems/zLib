package net.zargum.zlib.events;

import net.zargum.zlib.events.listeners.PlayerDamageByPlayerListener;
import net.zargum.zlib.events.listeners.SkinUpdateListener;
import net.zargum.zlib.menu.MenuListener;
import net.zargum.zlib.packets.PacketListener;
import net.zargum.zlib.teleport.TeleportListener;
import net.zargum.zlib.zLib;
import org.bukkit.event.Listener;

public class ListenersManager {

    private final zLib plugin;

    public ListenersManager(zLib plugin) {
        this.plugin = plugin;
        register(new SkinUpdateListener(plugin));
        register(new PlayerDamageByPlayerListener());
        register(new MenuListener(plugin));
        register(new TeleportListener(plugin));
        register(new PacketListener());
    }

    public void register(Listener listener){
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
