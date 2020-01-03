package net.zargum.plugin.zlib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.zargum.plugin.zlib.hologram.Hologram;
import net.zargum.plugin.zlib.utils.Reflections;
import net.zargum.plugin.zlib.zLib;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
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

    // TODO: Change Player object to UUID

    private JavaPlugin plugin;
    private UUID uniqueId;
    private Location location;
    private String displayName;
    private GameProfile gameProfile;
    private List<String> hologramLines;
    @Setter
    private ItemStack itemInHand;
    @Setter
    private ItemStack helmet;
    @Setter
    private ItemStack chestplate;
    @Setter
    private ItemStack leggings;
    @Setter
    private ItemStack boots;
    private String skinName;
    private Property skin;
    private Hologram hologram;
    private boolean hideName;
    private boolean tablist;
    private boolean created;
    private EntityArmorStand hideNameStand;
    private EntityPlayer entityNPC;
    private Map<Player, Boolean> players;
    private int unshowDistance = 45;
    private int showDistance = 32;

    public NPC(JavaPlugin plugin, String displayName) {
        this.plugin = plugin;
        this.displayName = displayName;
        this.uniqueId = UUID.randomUUID();
        this.gameProfile = new GameProfile(uniqueId, displayName);
        this.tablist = true;
        this.players = new HashMap<>();
        this.hologramLines = new ArrayList<>();
    }

    public NPC(JavaPlugin plugin) {
        this(plugin, RandomStringUtils.randomAlphanumeric(6));
    }

    public void setHologramLines(List<String> hologramLines) {
        this.hologramLines = hologramLines;
    }

    public void setHologramLines(String... hologramLines) {
        this.hologramLines = Arrays.asList(hologramLines);
    }

    public void addHologramLine(String hologram) {
        hologramLines.add(hologram);
    }

    public void setLocation(Location location) {
        Location loc = location.clone();
        loc.setX(location.getBlockX() + 0.5);
        loc.setZ(location.getBlockZ() + 0.5);
        this.location = loc;
    }
    public void updateLocation() {
        entityNPC.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );
    }

    public void setSkin(String usernameSkin, Property property) {
        this.skinName = usernameSkin;
        this.skin = property;
    }

    public void hideName() {
        this.hideName = true;
    }

    public boolean isNear(Player player) {
        boolean sameWorld = location.getWorld().equals(player.getWorld());
        return sameWorld && player.getLocation().distance(location) < unshowDistance;
    }

    public void removeFromTablist() {
        this.tablist = false;
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) if (isSpawnedTo(player)) unshow(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) if (isNear(player)) show(player);
        }, 5L);
    }

    public void show(Player player) {
        if (!this.created) throw new IllegalStateException("NPC not created");
        if (isSpawnedTo(player)) throw new IllegalStateException("NPC: Tried to show but is already spawned for " + player.getName());

        if (this.hologramLines.size() > 0) this.spawnHologram(player);
        if (this.skin != null) this.applySkin();
//        if (!gameProfile.getProperties().get("textures").contains(skin)) {
//            System.out.println("NPC GAMEPROFILE TEXTURE IS NOT EQUAL THAN SKIN");
//            entityNPC.getProfile().getProperties().removeAll("textures");
//            entityNPC.getProfile().getProperties().put("textures", skin);
//        }

        super.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityNPC), player);
        super.sendPacket(new PacketPlayOutNamedEntitySpawn(entityNPC), player);

        this.updateEquipment(player);
        if (this.hideName) hideDisplayName(player);
        if (!this.tablist) Bukkit.getScheduler().runTaskLaterAsynchronously(zLib.getInstance(), () -> hideFromTablist(player), 20L);
        this.players.put(player, true);
    }

    public void unshow(Player player) {
        if (!this.created) throw new IllegalStateException("NPC: NPC not created");
        if (this.players.containsKey(player) && !this.players.get(player)) {
            throw new IllegalStateException("NPC: Tried to unshow but not spawned for " + player.getName());
        }

        if (this.hologramLines.size() > 0) this.unshowHolograms(player);
        else if (this.hologram != null) {
            this.hologram.delete();
            this.hologram = null;
        }
        if (this.hideName) super.sendPacket(new PacketPlayOutEntityDestroy(hideNameStand.getId()), player);

        super.sendPacket(new PacketPlayOutEntityDestroy(entityNPC.getId()), player);
        super.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);

        this.players.put(player, false);
    }

    public void create() {
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        if (getSkin() != null) this.applySkin();
        this.entityNPC = new EntityPlayer(server, worldServer, this.gameProfile, new PlayerInteractManager(worldServer));
        this.entityNPC.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());

        // HideNameStand
        EntityArmorStand armorStand = new EntityArmorStand(worldServer, location.getX(), location.getY(), location.getZ());
        armorStand.setInvisible(true);
        armorStand.getBukkitEntity().setMetadata("HideNametag", new FixedMetadataValue(plugin, true));
        this.hideNameStand = armorStand;

        this.created = true;
    }

    public void delete() {
        if (!this.created) throw new IllegalStateException("NPC not created");
        this.deleteHolograms();
        for (Player player : players.keySet()) if (isSpawnedTo(player)) unshow(player);
    }

    public void updateEquipment(Player player) {
        // Set item in hand
        if (this.itemInHand != null) {
            int entityID = entityNPC.getBukkitEntity().getEntityId();
            super.sendPacket(new PacketPlayOutEntityEquipment(entityID, 0, CraftItemStack.asNMSCopy(itemInHand)), player);
        }

        // Set armour
        if (helmet != null)
            super.sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 4, CraftItemStack.asNMSCopy(helmet)), player);
        if (chestplate != null)
            super.sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 3, CraftItemStack.asNMSCopy(chestplate)), player);
        if (leggings != null)
            super.sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 2, CraftItemStack.asNMSCopy(leggings)), player);
        if (boots != null)
            super.sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 1, CraftItemStack.asNMSCopy(boots)), player);

    }

    public boolean isSpawnedTo(Player player) {
        return this.players.containsKey(player) && this.players.get(player);
    }

    // Utils methods
    private void applySkin() {
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", skin);
    }

    private void hideDisplayName(Player player) {
        super.sendPacket(new PacketPlayOutSpawnEntityLiving(hideNameStand), player);
        super.sendPacket(new PacketPlayOutAttachEntity(0, hideNameStand, entityNPC), player);
    }

    private void hideFromTablist(Player player) {
        if (!this.tablist) {
            Bukkit.getScheduler().runTaskLater(zLib.getInstance(), () -> {
                super.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);
            }, 10L);
        }
    }

    private void spawnHologram(Player player) {
        if (this.hologram == null) {
            Hologram hologram = new Hologram();
            hologram.setLines(hologramLines);
            hologram.setLocation(location.clone());
            hologram.create();
            hologram.show(player);
            this.hologram = hologram;
        } else {
            this.hologram.setLines(hologramLines);
            this.hologram.setLocation(location.clone());
            this.hologram.update(player);
        }
    }

    private void unshowHolograms(Player player) {
        if (!players.containsKey(player))
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but is not on players list.");
        if (this.hologram == null)
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but hologram == null");
        if (!hologram.isCreated())
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but hologram is not created.");

        this.hologram.unshow(player);
    }

    private void deleteHolograms() {
        if (this.hologram != null && hologram.isCreated()) {
            this.hologram.delete();
        }
    }

}
