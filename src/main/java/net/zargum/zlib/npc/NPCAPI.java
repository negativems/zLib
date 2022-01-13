package net.zargum.zlib.npc;

import lombok.Getter;
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
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class NPCAPI<T extends JavaPlugin> implements Listener {

    private final T plugin;
    protected final Map<String, NPC> NPCs = new HashMap<>();
    protected final Map<UUID, Long> recentlyClickPlayers = new HashMap<>();

    public NPCAPI(T plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

//    @EventHandler(priority = EventPriority.MONITOR)
//    public void onJoin(PlayerJoinEvent event) {
//        Player player = event.getPlayer();
//        for (NPC npc : NPCs.values()) {
//            if (!npc.isSpawnedTo(player) && npc.isNear(player)) {
//                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> npc.show(player), 20);
//            }
//        }
//    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            for (NPC npc : NPCs.values()) {
                npc.unshow(player);
                if (!npc.isSpawnedTo(player) && npc.isNear(player)) npc.show(player);
            }
        }, 10);
    }

    @EventHandler
    public void onPacketReceive(PacketReceiveEvent event) {
        Packet<?> packet = event.getPacket();
        Player player = event.getPlayer();
        if(packet.getClass().getSimpleName().equalsIgnoreCase("PacketPlayInUseEntity")){
            PacketPlayInUseEntity.EnumEntityUseAction action = (PacketPlayInUseEntity.EnumEntityUseAction) Reflections.getValue(packet, "action");
            int packetEntityId = (Integer) Reflections.getValue(packet, "a");

            for (NPC npc : NPCs.values()) {
                if (npc.getEntityNPC().getId() != packetEntityId || action != PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT) continue;

                UUID uniqueId = event.getPlayer().getUniqueId();
                if (recentlyClickPlayers.containsKey(uniqueId)) {
                    if (TimeUtils.getSecondsBetweenMillisAndNow(recentlyClickPlayers.get(uniqueId)) < 1L) {
                        return;
                    }
                }

                recentlyClickPlayers.put(uniqueId, System.currentTimeMillis());
                InteractNPCEvent interactNPCEvent = new InteractNPCEvent(player, npc);
                Bukkit.getPluginManager().callEvent(interactNPCEvent);
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for (NPC npc : NPCs.values()) {
            npc.getPlayers().remove(event.getPlayer());
            if (npc.getHologram() != null) {
                npc.getHologram().getPlayers().remove(event.getPlayer());
            }
        }
    }
}
