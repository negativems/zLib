package net.zargum.zlib.proxy;

import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ProxyHelper {

    private final JavaPlugin plugin;
    private ProxyHandler handler;

    public ProxyHelper(zLib plugin) {
        this.plugin = plugin;
        handler = plugin.getProxyHandler();
    }

    public void connectToServer(Player player, String server) {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF(server);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
    }

    public Integer getPlayersCount(String server) {
        if (!zLib.existsServer(server)) return 0;
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("PlayerCount");
                out.writeUTF(server);
                Bukkit.getServer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
        return handler.getServerPlayersCount().getOrDefault(server, 0);
    }

}
