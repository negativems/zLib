package net.zargum.plugin.zlib;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.zargum.plugin.zlib.menu.MenuListener;
import net.zargum.plugin.zlib.proxy.ProxyHandler;
import net.zargum.plugin.zlib.proxy.ProxyHelper;
import net.zargum.plugin.zlib.scoreboard.Scoreboard;
import net.zargum.plugin.zlib.teleport.TeleportListener;
import net.zargum.plugin.zlib.teleport.TeleportManager;
import net.zargum.plugin.zlib.teleport.TeleportTask;
import net.zargum.plugin.zlib.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class zLib extends JavaPlugin {

    @Getter private static zLib instance;
    @Getter private Map<UUID, Scoreboard> boards = new HashMap<>();
    @Getter private LuckPerms luckPermsApi;
    @Getter private ProxyHelper proxyHelper;
    public ProxyHandler proxyHandler;
    public static JedisPool pool;
    public String serverName;
    public TeleportManager teleportManager;
    public TeleportTask teleportTask;

    @Override
    public void onEnable() {
        instance = this;
        log(ChatColor.YELLOW + "Initializing...");
        serverName = getServer().getServerName();

        // Load Managers
        teleportManager = new TeleportManager(this);
        teleportTask = new TeleportTask(this);

        // Register Listeners
        getServer().getPluginManager().registerEvents(new MenuListener(), this);
        getServer().getPluginManager().registerEvents(new TeleportListener(this), this);

        // Bungee Helper
        log(ChatColor.YELLOW + "Register BungeeCord channel...");
        proxyHandler = new ProxyHandler(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", proxyHandler);
        proxyHelper = new ProxyHelper(this);
        proxyHandler.loadServers();

        // Load Redis
        log(ChatColor.YELLOW + "Setting up redis...");
        pool = new JedisPool("localhost", 6379);
        settingUpRedis();

        // Load LuckPermsApi
        log(ChatColor.YELLOW + "Loading permissionApi...");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPermsApi = provider.getProvider();

        // Task
        teleportTask.runTaskTimer(this, 0L, 20L);

        log(ChatColor.GREEN + "Initialized.");
    }

    @Override
    public void onDisable() {
        log(ChatColor.RED + "closing redis connection...");
        try (Jedis jedis = pool.getResource()) {
            String server = serverName;
            jedis.hset("status", server, "0");
        }
        pool.close();
        log(ChatColor.RED + getDescription().getName() + " has been disabled.");
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
            boolean whitelisted = proxyHelper.getWhitelistStatus(serverName);
            try (Jedis jedis = pool.getResource()) {
                jedis.hset("max_players", serverName, getServer().getMaxPlayers() + "");
                jedis.hset("status", serverName, whitelisted ? "1" : "2");
            }catch(Exception e){
                log("&cError setting up redis connection.");
            }
            log("&aServer is now online for the other servers.");
        });

    }

    public static int getMaxPlayers(String server) {
        try (Jedis jedis = pool.getResource()) {
            if (!jedis.hexists("max_players", server)) return -1;
            int result = Integer.parseInt(jedis.hget("max_players", server));
            return result;
        }
    }

    public static int getServerStatus(String server) {
        try (Jedis jedis = pool.getResource()) {
            if (!jedis.hexists("status", server)) return 0;
            int result = Integer.parseInt(jedis.hget("status", server));
            return result;
        }
    }

}
