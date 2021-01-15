package net.zargum.zlib;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.zargum.zlib.events.EventManager;
import net.zargum.zlib.menu.MenuListener;
import net.zargum.zlib.proxy.ProxyHandler;
import net.zargum.zlib.scoreboard.Scoreboard;
import net.zargum.zlib.skin.SkinManager;
import net.zargum.zlib.teleport.TeleportManager;
import net.zargum.zlib.teleport.TeleportTask;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;

@Getter
public class zLib extends JavaPlugin {

    @Getter private static zLib instance;
    private static final List<String> servers = new ArrayList<>();
    @Getter private final Map<UUID, Scoreboard> boards = new HashMap<>();
    @Getter private LuckPerms luckPermsApi;
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
        log("Initializing...");

        // Load config
        saveDefaultConfig();

        serverName = getServer().getServerName();
        if (serverName.equals("Unknown Server")) {
            log("");
            log(ChatColor.DARK_RED + "RENAME THE SERVER IN '&eserver.properties&c' CONFIG");
            log("");
            getServer().shutdown();
        }

        // Load Managers
        teleportManager = new TeleportManager(this);
        teleportTask = new TeleportTask(this);
        skinManager = new SkinManager(this);
        eventManager = new EventManager(this);

        // Load Listeners
        getServer().getPluginManager().registerEvents(new MenuListener(this), this);

        // Bungee Helper
        log("Register BungeeCord channel...");
        proxyHandler = new ProxyHandler(this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", proxyHandler);

        // Load Redis
        log("Setting up redis...");
        pool = new JedisPool(new GenericObjectPoolConfig(), getConfig().getString("redis.host"), getConfig().getInt("redis.port"), 2000, (getConfig().getBoolean("redis.auth.enabled") ? getConfig().getString("redis.auth.password") : null));
        settingUpRedis();

        // Load LuckPermsApi
        log("Loading permissionApi...");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPermsApi = provider.getProvider();


        // Task
        teleportTask.runTaskTimer(this, 0L, 20L);

        log(ChatColor.GREEN + "Loaded successfully.");
    }

    @Override
    public void onDisable() {
        log("Closing redis connection...");
        try (Jedis jedis = pool.getResource()) {
            String server = serverName;
            jedis.hset("status", server, "0");
        }
        pool.close();
        log("Saving skins...");
        skinManager.save();
        log(ChatColor.RED + getDescription().getName() + " has been disabled.");
    }

    public static void log(String message) {
        ConsoleCommandSender console = zLib.getInstance().getServer().getConsoleSender();
        console.sendMessage(ChatColor.GRAY + "[" + ChatColor.LIGHT_PURPLE + "zLib" + ChatColor.GRAY + "] " + ChatColor.YELLOW + message);
    }

    private void settingUpRedis() {
        log("Waiting server completly load set status to online...");
        try (Jedis jedis = pool.getResource()) {
            jedis.hset("status", serverName, "-1");
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
            try (Jedis jedis = pool.getResource()) {
                jedis.hset("max_players", serverName, getServer().getMaxPlayers() + "");
                jedis.hset("status", serverName, "2");
            } catch (Exception e) {
                log(ChatColor.RED + "Error setting up redis connection.");
                e.printStackTrace();
            }
            log(ChatColor.GREEN + "Server is now online for the other servers.");
        }, 20 * 2);
    }

}
