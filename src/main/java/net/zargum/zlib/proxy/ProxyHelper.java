package net.zargum.zlib.proxy;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.zargum.zlib.messages.Messages;
import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import redis.clients.jedis.Jedis;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.Objects;
import java.util.Set;

public class ProxyHelper {

    public static ServerInfo getServerInfo(String serverName) {
        try (Jedis jedis = zLib.pool.getResource()) {
            if (!jedis.hexists("servers", serverName)) return null;
            String jsonString = jedis.hget("servers", serverName);
            JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
            return new ServerInfo(serverName, ServerStatusType.ENUM_VALUES.get(jsonObject.get("status").getAsInt()), jsonObject.get("online").getAsInt());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Set<String> getAllServers() {
        try (Jedis jedis = zLib.pool.getResource()) {
            return jedis.hgetAll("servers").keySet();
        }
    }

    public static void connectToServer(JavaPlugin plugin, Player player, String serverName) {
        if (!existsServer(serverName)) return;
        ServerInfo serverInfo = getServerInfo(serverName);
        switch (Objects.requireNonNull(serverInfo).getStatus()) {
            case OFFLINE:
                player.sendMessage(Messages.CONNECT_OFFLINE.toString(serverName));
                return;
            case INITIALIZING:
                player.sendMessage(Messages.CONNECT_INITIALIZING.toString(serverName));
                return;
        }

        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("Connect");
                out.writeUTF(serverName);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
    }

    public static boolean existsServer(String serverName) {
        return serverName.equals("ALL") || getServerInfo(serverName) != null;
    }

    public static Integer getMaxPlayers(String server) {
        if (!existsServer(server)) return -1;
        try (Jedis jedis = zLib.pool.getResource()) {
            if (!jedis.hexists("max_players", server)) return -1;
            return Integer.parseInt(jedis.hget("max_players", server));
        }
    }

    public static ServerStatusType getServerStatus(String serverName) {
        if (!existsServer(serverName)) return ServerStatusType.OFFLINE;
        return Objects.requireNonNull(getServerInfo(serverName)).getStatus();
    }

    public static int getAllPlayersOnline() {
        int result = 0;
        Set<String> servers = getAllServers();
        for (String serverName : servers) {
            if (!existsServer(serverName)) continue;
            result = result + Objects.requireNonNull(getServerInfo(serverName)).getOnlineCount();
        }
        return result;
    }

}
