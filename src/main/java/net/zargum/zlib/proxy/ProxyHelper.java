package net.zargum.zlib.proxy;

import net.zargum.zlib.servers.ServerStatus;
import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class ProxyHelper {

    public static void connectToServer(JavaPlugin plugin, Player player, String server) {
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

    public static int getPlayersCount(JavaPlugin plugin, String server) {
        if (!ProxyHelper.existsServer(server)) return 0;
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
        return zLib.getInstance().getProxyHandler().getServerPlayersCount().getOrDefault(server, 0);
    }

    public static boolean existsServer(String server) {
        if (server.equals("ALL")) return true;
        try (Jedis jedis = zLib.pool.getResource()) {
            return jedis.hexists("status", server);
        } catch (Exception e) {
            zLib.log(ChatColor.RED + "Error setting up redis connection.");
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getMaxPlayers(String server) {
        if (!existsServer(server)) return -1;
        try (Jedis jedis = zLib.pool.getResource()) {
            if (!jedis.hexists("max_players", server)) return -1;
            return Integer.parseInt(jedis.hget("max_players", server));
        }
    }

    public static ServerStatus getServerStatus(String server) {
        if (!existsServer(server)) return ServerStatus.OFFLINE;
        try (Jedis jedis = zLib.pool.getResource()) {
            if (!jedis.hexists("status", server)) return ServerStatus.OFFLINE;
            return ServerStatus.getById(Integer.parseInt(jedis.hget("status", server)));
        }
    }

}
