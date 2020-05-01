package net.zargum.zlib.events;

import net.zargum.zlib.events.listeners.PlayerDamageByPlayerListener;
import net.zargum.zlib.menu.MenuListener;
import net.zargum.zlib.teleport.TeleportListener;
import net.zargum.zlib.zLib;
import org.bukkit.event.Listener;

public class EventManager {

    private final zLib plugin;

    public EventManager(zLib plugin) {
        this.plugin = plugin;
        register(new PlayerDamageByPlayerListener());
        register(new MenuListener());
        register(new TeleportListener(plugin));
    }

    public void register(Listener listener){
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
