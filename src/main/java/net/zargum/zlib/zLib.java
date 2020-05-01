package net.zargum.zlib;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.zargum.zlib.events.EventManager;
import net.zargum.zlib.proxy.ProxyHandler;
import net.zargum.zlib.proxy.ProxyHelper;
import net.zargum.zlib.scoreboard.Scoreboard;
import net.zargum.zlib.skin.SkinManager;
import net.zargum.zlib.teleport.TeleportManager;
import net.zargum.zlib.teleport.TeleportTask;
import net.zargum.zlib.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Getter
public class zLib extends JavaPlugin {

    @Getter
    private static zLib instance;
    private static final List<String> servers = new ArrayList<>();
    @Getter
    private final Map<UUID, Scoreboard> boards = new HashMap<>();
    @Getter
    private LuckPerms luckPermsApi;
    @Getter
    private ProxyHelper proxyHelper;
    public ProxyHandler proxyHandler;
    public static JedisPool pool;
    public String serverName;
    public TeleportManager teleportManager;
    public TeleportTask teleportTask;
    public SkinManager skinManager;
    public EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        log("&eInitializing...");
        serverName = getServer().getServerName();
        if (serverName.equals("Unknown Server")) {
            log("&c");
            log("&c RENAME THE SERVER IN '&eserver.properties&c' CONFIG");
            log("&c");
            getServer().shutdown();
        }

        // Load Managers
        teleportManager = new TeleportManager(this);
        teleportTask = new TeleportTask(this);
        skinManager = new SkinManager(this);
        eventManager = new EventManager(this);

        // Bungee Helper
        log("&eRegister BungeeCord channel...");
        proxyHandler = new ProxyHandler(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", proxyHandler);
        proxyHelper = new ProxyHelper(this);

        // Load Redis
        log("&eSetting up redis...");
        pool = new JedisPool("localhost", 6379);
        settingUpRedis();

        // Load LuckPermsApi
        log("&eLoading permissionApi...");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPermsApi = provider.getProvider();

        // Task
        teleportTask.runTaskTimer(this, 0L, 20L);

        log("&aInitialized.");
    }

    @Override
    public void onDisable() {
        log("&eclosing redis connection...");
        try (Jedis jedis = pool.getResource()) {
            String server = serverName;
            jedis.hset("status", server, "0");
        }
        pool.close();
        log("&eSaving skins...");
        skinManager.save();
        log("&c" + getDescription().getName() + " has been disabled.");
    }

    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(ColorUtils.translate("&e[&6zLib&e] &e" + s));
    }

    private void settingUpRedis() {
        log("&eWaiting server completly load set status to online...");
        try (Jedis jedis = pool.getResource()) {
            jedis.hset("status", serverName, "-1");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.hset("max_players", serverName, getServer().getMaxPlayers() + "");
                jedis.hset("status", serverName, "2");
            } catch (Exception e) {
                log("&cError setting up redis connection.");
            }
            log("&aServer is now online for the other servers.");
        }, 20 * 2);
    }

    public static boolean existsServer(String server) {
        if (server.equals("ALL")) return true;
        try (Jedis jedis = pool.getResource()) {
            return jedis.hexists("status", server);
        } catch (Exception e) {
            log("&cError setting up redis connection.");
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

    public static Integer getServerStatus(String server) {
        if (!existsServer(server)) return 0;
        try (Jedis jedis = zLib.pool.getResource()) {
            if (!jedis.hexists("status", server)) return 0;
            return Integer.parseInt(jedis.hget("status", server));
        }
    }

}
