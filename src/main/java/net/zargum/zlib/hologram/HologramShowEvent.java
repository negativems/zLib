package net.zargum.zlib.hologram;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.zargum.zlib.utils.Reflections;
import net.zargum.zlib.zLib;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HologramShowEvent extends Event implements Cancellable {

    private static final HandlerList HANDLERS = new HandlerList();
    @Getter
    private final Player player;
    @Getter
    private final Hologram hologram;
    @Getter
    private Map<String, String> replaceLinesMap = new HashMap<>();
    @Getter
    @Setter
    private boolean cancelled;

    public HologramShowEvent(Player player, Hologram hologram) {
        this.player = player;
        this.hologram = hologram;

        if (!hologram.isSpawnedTo(player)) {
            for (int i = hologram.getEntities().size() - 1; i >= 0; i--) {
                EntityArmorStand entity = hologram.getEntities().get(i);
                if (entity == null) continue;
                Location location = hologram.getLocation().clone();
                entity.setLocation(location.getX(), location.getY() + (hologram.getLinesDistance() * (hologram.getEntities().size() - (i - 1))), location.getZ(), 0, 0);
                Reflections.sendPacket(new PacketPlayOutSpawnEntityLiving(entity), player);
            }
            hologram.getPlayers().put(player, true);
        } else {
            show();
        }
    }

    public void setReplaceLinesMap(Map<String, String> replaceLinesMap) {
        this.replaceLinesMap = replaceLinesMap;
    }

    public void show() {
        int i;
        int entitiesAmount = hologram.getEntities().size();
        List<String> lines = hologram.getLines();
        Location location = hologram.getLocation().clone();

        // More entities than lines (kill entities to player)
        if (lines.size() < entitiesAmount) {
            for (i = hologram.getEntities().size(); i > lines.size(); i--) {
                EntityArmorStand entity = hologram.getEntities().get(i - 1);
                hologram.getEntities().remove(i - 1);
                if (entity == null) continue;
                Reflections.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()), player);
                entity.getBukkitEntity().remove();
            }
        }

        // More lines than entities (kill entities to player)
        if (lines.size() > entitiesAmount) {
            for (i = hologram.getEntities().size(); i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.isEmpty()) {
                    hologram.getEntities().add(null);
                    continue;
                }
                WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
                EntityArmorStand entity = new EntityArmorStand(worldServer, location.getX(), location.getY() + (hologram.getLinesDistance() * (lines.size() - (i - 1))), location.getZ());
                entity.setCustomName(line);
                entity.setCustomNameVisible(true);
                entity.setGravity(false);
                entity.setInvisible(true);
                entity.setBasePlate(false);
                entity.setArms(false);
                entity.setSmall(true);
                if (hologram.getEntities().size() == i) {
                    hologram.getEntities().add(entity);
                } else {
                    hologram.getEntities().set(i, entity);
                }

                Reflections.sendPacket(new PacketPlayOutSpawnEntityLiving(entity), player);
            }
        }

        for (i = lines.size() - 1; i >= 0; i--) {
            String line = hologram.getLine(i);
            EntityArmorStand entity = i >= hologram.getEntities().size() ? null : hologram.getEntities().get(i);
            // Line is not empty but entity is null (spawn the entity where is)
            if (!line.isEmpty() && entity == null) {
                WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
                entity = new EntityArmorStand(worldServer, location.getX(), location.getY() + (hologram.getLinesDistance() * (lines.size() - (i - 1))), location.getZ());
                entity.setCustomName(line);
                entity.setCustomNameVisible(true);
                entity.setGravity(false);
                entity.setInvisible(true);
                entity.setBasePlate(false);
                entity.setArms(false);
                entity.setSmall(true);
                if (hologram.getEntities().size() == i) hologram.getEntities().add(entity);
                else hologram.getEntities().set(i, entity);
                Reflections.sendPacket(new PacketPlayOutSpawnEntityLiving(entity), player);
                continue;
            }
            if (line.isEmpty() && entity == null) continue;
            if (entity.getCustomName().equals(line)) continue;
            if (line.isEmpty()) {
                hologram.getEntities().set(i, null);
                Reflections.sendPacket(new PacketPlayOutEntityDestroy(entity.getId()), player);
                entity.getBukkitEntity().remove();
                continue;
            }
            zLib.log(ChatColor.YELLOW + "Changed armor stand's name from '" + entity.getCustomName() + "' to '" + line + "'");
            hologram.getEntities().get(i).setCustomName(line);
        }

        for (i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            if (line.isEmpty()) continue;

            EntityArmorStand entity = hologram.getEntities().get(i);

            // Update location
            Location entityLocation = new Location(entity.world.getWorld(), entity.locX, location.getY() + (hologram.getLinesDistance() * (lines.size() - (i - 1))), entity.locZ);
            if (!location.equals(entityLocation)) {
                entity.locX = location.getX();
                entity.locY = location.getY() + (hologram.getLinesDistance() * (lines.size() - (i - 1)));
                entity.locZ = location.getZ();
                Reflections.sendPacket(new PacketPlayOutEntityTeleport(entity), player);
            }

            // Replace lines
            for (String variable : replaceLinesMap.keySet()) {
                String replacement = replaceLinesMap.get(variable);
                line = line.replaceAll(variable, replacement);
            }
            if (line.contains("%")) continue;

            DataWatcher datawatcher = new DataWatcher(entity);
            datawatcher.a(2, line);
            PacketPlayOutEntityMetadata packet = new PacketPlayOutEntityMetadata(entity.getId(), datawatcher, true);
            Reflections.sendPacket(packet, player);
        }
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}