package net.zargum.plugin.zlib.hologram;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.zargum.plugin.zlib.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class Hologram extends Reflections {

    // TODO: Change Player object to UUID

    private double linesdistance;
    private boolean created;

    private List<EntityArmorStand> armorStands;
    private Location location;
    private List<String> lines;
    private Map<Player, Boolean> players;

    public Hologram() {
        this.created = false;
        this.linesdistance = 0.24;
        this.armorStands = new ArrayList<>();
        this.lines = new ArrayList<>();
        this.players = new HashMap<>();
    }

    public Hologram(String firstLine) {
        this();
        this.lines.add(firstLine);
    }


    public Hologram setLocation(Location location) {
        this.location = location.clone();
        this.location.setY(location.getY() + (linesdistance * lines.size()) + 0.8);
        return this;
    }

    public Hologram setLines(List<String> lines) {
        this.lines = lines;
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

    public boolean isSpawnedTo(Player player) {
        return this.players.containsKey(player) && this.players.get(player);
    }

    public void update(Player player) {
        if (isSpawnedTo(player)) unshow(player);
        for (EntityArmorStand stand : armorStands) stand.getBukkitEntity().remove();
        this.armorStands = new ArrayList<>();
        this.create();
        this.show(player);
    }

    public String getLine(int index) {
        return this.lines.get(index);
    }

    public void create() {
        int i = 1;
        for (String line : lines) {
            Location loc = location.clone();
            WorldServer worldServer = ((CraftWorld) loc.getWorld()).getHandle();
            EntityArmorStand stand = new EntityArmorStand(worldServer);
            stand.setLocation(loc.getX(), loc.getY() - (linesdistance * i), loc.getZ(), 0, 0);
            stand.setCustomName(line);
            stand.setCustomNameVisible(true);
            stand.setGravity(false);
            stand.setSmall(true);
            stand.setInvisible(true);
            stand.setBasePlate(false);
            stand.setArms(false);

            this.armorStands.add(stand);
            i++;
        }
        this.created = true;
    }

    public void delete() {
        if (!this.created) throw new IllegalStateException("Hologram: Hologram not created.");
        for (Player player : Bukkit.getOnlinePlayers()) if (isSpawnedTo(player)) unshow(player);
        for (EntityArmorStand stand : armorStands) stand.getBukkitEntity().remove();
        this.armorStands = null;
        this.lines = null;
        this.location = null;
        this.created = false;
        this.players = null;
    }

    public void show(Player player) {
        if (!this.created) throw new IllegalStateException("Hologram: Hologram not created");
        if (isSpawnedTo(player)) throw new IllegalStateException("Hologram: Tried to show but is already spawned for " + player.getName());

        for (EntityArmorStand stand : armorStands) super.sendPacket(new PacketPlayOutSpawnEntityLiving(stand), player);
        this.players.put(player, true);
    }

    public void unshow(Player player) {
        if (!this.created) throw new IllegalStateException("Hologram: hologram not created");
        if (!isSpawnedTo(player)) throw new IllegalStateException("Hologram: Tried to unshow but is not spawned for " + player.getName());

        for (EntityArmorStand stand : armorStands) super.sendPacket(new PacketPlayOutEntityDestroy(stand.getId()), player);
        this.players.put(player, false);
    }
}
