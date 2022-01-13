package net.zargum.zlib.hologram;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.zargum.zlib.packets.PacketReceiveEvent;
import net.zargum.zlib.utils.Reflections;
import net.zargum.zlib.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HologramAPI<T extends JavaPlugin> implements Listener {

    private final T plugin;
    @Getter protected final Map<String, Hologram> hologramsMap = new HashMap<>();
    @Getter protected final Map<UUID, Long> recentlyClickPlayers = new HashMap<>();

    public HologramAPI(T plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (Hologram hologram : hologramsMap.values()) {
            if (!hologram.isSpawnedTo(player) && hologram.isNear(player)) hologram.show(player);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            for (Hologram hologram : hologramsMap.values()) {
                hologram.unshow(player);
                if (!hologram.isSpawnedTo(player) && hologram.isNear(player)) hologram.show(player);
            }
        }, 10);
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        Packet<?> packet = event.getPacket();
        Player player = event.getPlayer();
        if (packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")) {
            PacketPlayInUseEntity.EnumEntityUseAction action = (PacketPlayInUseEntity.EnumEntityUseAction) Reflections.getValue(packet, "action");
            int packetEntityId = (Integer) Reflections.getValue(packet, "a");
            if (action != PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) return;

            for (Hologram hologram : hologramsMap.values()) {
                for (EntityArmorStand entity : hologram.getEntities()) {
                    if (entity == null) continue;
                    if (entity.getId() != packetEntityId) continue;
                    UUID uniqueId = event.getPlayer().getUniqueId();
                    if (recentlyClickPlayers.containsKey(uniqueId)) {
                        if (TimeUtils.getSecondsBetweenMillisAndNow(recentlyClickPlayers.get(uniqueId)) < 1L) {
                            return;
                        }
                    }

                    recentlyClickPlayers.put(uniqueId, System.currentTimeMillis());
                    InteractHologramEvent interactHologramEvent = new InteractHologramEvent(player, hologram);
                    Bukkit.getPluginManager().callEvent(interactHologramEvent);
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for (Hologram hologram : hologramsMap.values()) {
            hologram.getPlayers().remove(event.getPlayer());
        }
    }

}
