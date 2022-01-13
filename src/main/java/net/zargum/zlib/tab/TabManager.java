package net.zargum.zlib.tab;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TabManager implements Listener {

    @Getter private final JavaPlugin plugin;
    private final Map<UUID, Tab> tabs;
    @Getter @Setter private TabAdapter adapter;

    public TabManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.tabs = new ConcurrentHashMap<>();

        if (Bukkit.getMaxPlayers() < 60) {
            Bukkit.getLogger().severe("There aren't 60 player slots, this will fuck up the tab list."); //TODO: Possibly set max players to 60?
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            // if (((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() < 47) {
            if (!(tabs.containsKey(player.getUniqueId()))) {
                tabs.put(player.getUniqueId(), new Tab(player, true, this));
            }
            // }
        }

        new TabTask(this);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public TabManager(JavaPlugin plugin, TabAdapter adapter) {
        this(plugin);

        this.adapter = adapter;
    }

    public Tab getTabByPlayer(Player player) {
        return tabs.get(player.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        for (Player online : Bukkit.getOnlinePlayers()) {
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
            ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                tabs.put(player.getUniqueId(), new Tab(player, true, TabManager.this));
            }
        }.runTaskLater(plugin, 1L);
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        tabs.remove(event.getPlayer().getUniqueId());

        for (Player other : Bukkit.getOnlinePlayers()) {
            EntityPlayer entityPlayer = ((CraftPlayer)other).getHandle();

            Tab tab = getTabByPlayer(event.getPlayer());

            if (tab != null && tab.getElevatedTeam() != null) {
                tab.getElevatedTeam().removeEntry(event.getPlayer().getName());
            }


        }
    }

}
