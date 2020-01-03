package net.zargum.plugin.zlib;

import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.zargum.plugin.zlib.proxy.ProxyHelper;
import net.zargum.plugin.zlib.scoreboard.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class zLib extends JavaPlugin {

    @Getter private static zLib instance;
    @Getter private Map<UUID, Scoreboard> boards = new HashMap<>();
    @Getter private LuckPerms luckPermsApi;
    @Getter private ProxyHelper proxy;

    @Override
    public void onEnable() {
        instance = this;
        log(ChatColor.YELLOW + "Initializing...");

        // Load LuckPermsApi
        log(ChatColor.YELLOW + "Loading permissionApi...");
        RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (provider != null) luckPermsApi = provider.getProvider();

        // Bungee Helper
        this.proxy = new ProxyHelper(this);
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", proxy);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

        log(ChatColor.GREEN + "Initialized.");
    }

    @Override
    public void onDisable() {
        log(ChatColor.RED + getDescription().getName() + " has been disabled.");
    }
    public static void log(String s) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[zLib] " + s);
    }
}
