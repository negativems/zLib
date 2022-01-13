package net.zargum.zlib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.zargum.zlib.hologram.Hologram;
import net.zargum.zlib.skin.SkinUtil;
import net.zargum.zlib.utils.ColorUtils;
import net.zargum.zlib.utils.Reflections;
import net.zargum.zlib.zLib;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class NPC extends Reflections {

    public final JavaPlugin plugin;

    private NPCEntity entityNPC;
    private final UUID uniqueID;
    private final GameProfile gameProfile;
    @Setter private Location location;
    @Setter private ItemStack itemInHand;
    @Setter private ItemStack[] armor;
    private String displayName;
    private final String id;
    private Property skinProperty;
    private Hologram hologram;
    private EntityArmorStand hideNameStand;
    private boolean hideName, created, tablisted = true;
    private boolean selfSkin = false;
    private final Map<Player, Boolean> players = new HashMap<>();
    private final int unshowDistance = 45;
    private final int showDistance = 32;
    private final List<InteractHandler> interactHandlers = new ArrayList<>();

    public NPC(@NonNull JavaPlugin plugin, @NonNull String id, @NonNull Location location) {
        this.plugin = plugin;
        this.id = id;
        this.location = location.clone();
        displayName = id;
        uniqueID = UUID.randomUUID();
        gameProfile = new GameProfile(uniqueID, id);
    }

    public NPC(JavaPlugin plugin, Location location) {
        this(plugin, RandomStringUtils.randomAlphanumeric(6), location);
    }

    public NPC attachHologram(Hologram hologram) {
        this.hologram = hologram;
        if (hologram != null) hologram.setLocation(location.clone().add(0, hologram.getLinesDistance(), 0));
        return this;
    }

    public void setSkinProperty(Property skinProperty) {
        this.skinProperty = skinProperty;
    }

    public void setSelfSkin(boolean selfSkin) {
        this.selfSkin = selfSkin;
    }

    public void setDisplayName(String displayName) {
        this.displayName = ColorUtils.translate(displayName);
        if (isCreated()) entityNPC.setCustomName(displayName);
    }

    public void hideName() {
        hideName = true;
    }

    public NPC unhideName() {
        hideName = false;
        return this;
    }

    public boolean isNear(Player player) {
        boolean sameWorld = location.getWorld().equals(player.getWorld());
        return sameWorld && player.getLocation().distance(location) < unshowDistance;
    }

    public void removeFromTablist() {
        tablisted = false;
    }

    public void updateLocation(Location location) {
        for (Player player : players.keySet()) {
            if (isSpawnedTo(player)) {
                unshow(player);
            }
        }
        for (Player player : hologram.getPlayers().keySet()) {
            if (hologram.isSpawnedTo(player)) {
                hologram.unshow(player);
            }
        }

        hologram.setLocation(location);
        entityNPC.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
        this.location = location.clone();
    }

    public void update(Player player) {
        if (isSpawnedTo(player)) {
            unshow(player);
        }
        if (isNear(player)) {
            show(player);
        }
    }

    public void update() {
        int i = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> update(player), i * 3L);
            i++;
        }
    }

    // TODO: Check if skins not lag the server, if it lags, make the method async.
    public void show(Player player) {
        if (!created) {
            throw new IllegalStateException("NPC not created");
        }
        if (isSpawnedTo(player)) {
            throw new IllegalStateException("NPC: Tried to show but is already spawned for " + player.getName());
        }

        if (hologram != null) hologram.update(player);
        if (selfSkin) applySkin(player.getName());
        if (!selfSkin && skinProperty != null) applySkin();
        if (displayName != null) entityNPC.setCustomName(displayName);

        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityNPC), player);
        sendPacket(new PacketPlayOutNamedEntitySpawn(entityNPC), player);
        sendPacket(new PacketPlayOutEntityHeadRotation(entityNPC, getFixRotation(location.getYaw())), player);
        sendPacket(new PacketPlayOutAnimation(entityNPC, 0), player);

        if (armor != null && armor.length > 0) updateEquipment(player);
        if (hideName) hideDisplayName(player);
        if (!tablisted) hideFromTablist(player);
        players.put(player, true);
    }

    public void show() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isNear(player)) {
                show(player);
            }
        }
    }

    public void unshow(Player player) {
        if (!created) throw new IllegalStateException("NPC: NPC not created");
        if (players.containsKey(player) && !players.get(player)) {
            throw new IllegalStateException("NPC: Tried to unshow but not spawned for " + player.getName());
        }

        if (hologram != null && hologram.isSpawnedTo(player)) unshowHolograms(player);
        if (hideName) sendPacket(new PacketPlayOutEntityDestroy(hideNameStand.getId()), player);

        sendPacket(new PacketPlayOutEntityDestroy(entityNPC.getId()), player);
        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);

        players.put(player, false);
    }

    public void create() {
        MinecraftServer server = MinecraftServer.getServer();
        if (location == null || location.getWorld() == null) {
            zLib.log(ChatColor.RED + "Error creating NPC " + id + ": world is not exists");
            return;
        }
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        if (getSkinProperty() != null) applySkin();

        entityNPC = new NPCEntity(server, worldServer, gameProfile);
        entityNPC.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        // Outside skin
        byte flags = 0x01 | 0x02 | 0x04 | 0x08 | 0x10 | 0x20 | 0x40;
        entityNPC.getDataWatcher().watch(10, flags);

        // HideNameStand
        EntityArmorStand armorStand = new EntityArmorStand(worldServer, location.getX(), location.getY(), location.getZ());
        armorStand.setInvisible(true);
        armorStand.getBukkitEntity().setMetadata("HideNametag", new FixedMetadataValue(plugin, true));
        hideNameStand = armorStand;

        created = true;
    }

    public void delete() {
        if (!created) throw new IllegalStateException("NPC not created");
        for (Player player : players.keySet()) if (isSpawnedTo(player)) unshow(player);
        deleteHolograms();
    }

    public void updateEquipment(Player player) {
        int entityId = entityNPC.getBukkitEntity().getEntityId();
        if (armor[3] != null) {
            sendPacket(new PacketPlayOutEntityEquipment(entityId, 4, CraftItemStack.asNMSCopy(armor[3])), player);
        }
        if (armor[2] != null) {
            sendPacket(new PacketPlayOutEntityEquipment(entityId, 3, CraftItemStack.asNMSCopy(armor[2])), player);
        }
        if (armor[1] != null) {
            sendPacket(new PacketPlayOutEntityEquipment(entityId, 2, CraftItemStack.asNMSCopy(armor[1])), player);
        }
        if (armor[0] != null) {
            sendPacket(new PacketPlayOutEntityEquipment(entityId, 1, CraftItemStack.asNMSCopy(armor[0])), player);
        }
        if (itemInHand != null) {
            sendPacket(new PacketPlayOutEntityEquipment(entityId, 0, CraftItemStack.asNMSCopy(itemInHand)), player);
        }
    }

    public boolean isSpawnedTo(Player player) {
        return players.containsKey(player) && players.get(player);
    }

    // Utils methods
    private int getFixLocation(double pos) {
        return MathHelper.floor(pos * 32.0D);
    }

    private byte getFixRotation(float angle) {
        return (byte) ((int) (angle * 256.0F / 360.0F));
    }

    private void applySkin(String username) {
        Property property = SkinUtil.getProperty(username);
        if (property == null) throw new NullPointerException();
        skinProperty = property;
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", property);
    }

    private void applySkin() {
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", skinProperty);
    }

    private void hideDisplayName(Player player) {
        sendPacket(new PacketPlayOutSpawnEntityLiving(hideNameStand), player);
        sendPacket(new PacketPlayOutAttachEntity(0, hideNameStand, entityNPC), player);
    }

    private void hideFromTablist(Player player) {
        if (!tablisted) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(zLib.getInstance(), () -> sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player), 20L);
        }
    }

    private void unshowHolograms(Player player) {
        if (!players.containsKey(player))
            throw new IllegalStateException("NPC: Tried to unshow holograms to " + player.getName() + " but is not on players list.");
        if (hologram == null)
            throw new IllegalStateException("NPC: Tried to unshow holograms to " + player.getName() + " but hologram == null");
        if (!hologram.isCreated())
            throw new IllegalStateException("NPC: Tried to unshow holograms to " + player.getName() + " but hologram is not created.");
        if (!hologram.isSpawnedTo(player))
            throw new IllegalStateException("NPC: Tried to unshow holograms to " + player.getName() + " but is not spawned.");

        hologram.unshow(player);
    }

    private void deleteHolograms() {
        if (hologram != null && hologram.isCreated()) {
            hologram.delete();
        }
        hologram = null;
    }

    public void addInteractHandler(InteractHandler handler) {
        if (interactHandlers.contains(handler)) return;

        interactHandlers.add(handler);
    }

    public void removeInteractHandler(InteractHandler handler) {
        interactHandlers.remove(handler);
    }

}
