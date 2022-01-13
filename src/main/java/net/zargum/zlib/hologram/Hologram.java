package net.zargum.zlib.hologram;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.zargum.zlib.utils.Reflections;
import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class Hologram extends Reflections {

    // TODO: Change Player object to UUID
    private final JavaPlugin plugin;

    private final String id;
    private final double linesDistance = 0.26;
    private boolean created, autoUpdate = false;
    private List<EntityArmorStand> entities = new ArrayList<>();
    private Location location;
    private List<String> lines = new ArrayList<>();
    private Map<Player, Boolean> players = new HashMap<>();
    private final int unshowDistance = 45;
    private final int showDistance = 32;

    public Hologram(JavaPlugin plugin, String id, Location location) {
        this.plugin = plugin;
        this.id = id;
        this.location = location.clone();
    }

    public Hologram setAutoUpdate(boolean state) {
        this.autoUpdate = state;
        return this;
    }

    public Hologram setLocation(Location location) {
        this.location = location.clone();
//        for (Player player : Bukkit.getOnlinePlayers()) {
//            if (isSpawnedTo(player)) {
//                unshow(player);
//                update(player);
//            }
//        }
        return this;
    }

    public Hologram setLine(int lineIndex, String line) {
        if (line.isEmpty()) {
            EntityArmorStand entity = entities.get(lineIndex);
            for (Player player : Bukkit.getOnlinePlayers()) {
                Reflections.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()), player);
            }
        }
        this.lines.set(lineIndex, line);
        return this;
    }

    public Hologram setLines(List<String> lines) {
        this.lines = new ArrayList<>(lines);
        return this;
    }

    public Hologram setLines(String[] lines) {
        this.lines = Arrays.asList(lines);
        return this;
    }

    public Hologram addLine(String line) {
        this.lines.add(line);
        return this;
    }

    public Hologram removeLine(int index) {
        this.lines.remove(index);
        return this;
    }
    public boolean isNear(Player player) {
        boolean sameWorld = location.getWorld().equals(player.getWorld());
        return sameWorld && player.getLocation().distance(location) < unshowDistance;
    }

    public boolean isSpawnedTo(Player player) {
        return this.players.containsKey(player) && this.players.get(player);
    }

    public void update(Player player) {
       if (isNear(player)) show(player);
    }

    public void update() {
        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin,() -> {
                update(player);
            }, i);
            i = i + 3;
        }
    }

    public String getLine(int index) {
        return this.lines.get(index);
    }

    public void create() {
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.isEmpty()) {
                entities.add(null);
                continue;
            }
            Location loc = location.clone();
            WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
            EntityArmorStand entity = new EntityArmorStand(worldServer, loc.getX(), loc.getY() - (linesDistance * (i + 1)), loc.getZ());
            entity.setCustomName(line);
            entity.setCustomNameVisible(true);
            entity.setGravity(false);
            entity.setInvisible(true);
            entity.setBasePlate(false);
            entity.setArms(false);
            entity.setSmall(true);
            this.entities.add(entity);
        }
        this.created = true;
    }

    public void delete() {
        if (!this.created) throw new IllegalStateException("Hologram: Hologram not created.");
        for (Player player : Bukkit.getOnlinePlayers()) if (isSpawnedTo(player)) unshow(player);
        for (EntityArmorStand stand : entities) {
            if (stand == null) continue;
            stand.getBukkitEntity().remove();
        }
        this.entities = null;
        this.lines = null;
        this.location = null;
        this.created = false;
        this.players = null;
    }

    public void show(Player player) {
        if (player == null || !player.isOnline()) {
            zLib.log(ChatColor.RED + "Player is null");
            players.remove(player);
            return;
        }
        if (!this.created) throw new IllegalStateException("Hologram: Hologram not created");
//        if (isSpawnedTo(player)) throw new IllegalStateException("Hologram: Tried to show but is already spawned for " + player.getName());

        Bukkit.getPluginManager().callEvent(new HologramShowEvent(player, this));
        this.players.put(player, true);
    }

    public void show() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNear(player)) {
                show(player);
            }
        }
    }

    public void unshow(Player player) {
        if (!this.created) throw new IllegalStateException("Hologram: hologram not created");
        if (!isSpawnedTo(player)) throw new IllegalStateException("Hologram: Tried to unshow hologram but is not spawned for " + player.getName());

        Bukkit.getPluginManager().callEvent(new HologramUnshowEvent(player, this));
        this.players.put(player, false);
    }
}
