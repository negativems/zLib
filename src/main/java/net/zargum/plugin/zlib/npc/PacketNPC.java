package net.zargum.plugin.zlib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.*;
import net.zargum.plugin.zlib.hologram.Hologram;
import net.zargum.plugin.zlib.utils.Reflections;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

@Getter
public class PacketNPC extends Reflections {

    // TODO: Change Player object to UUID

    private JavaPlugin plugin;

    private int entityID;
    private GameProfile gameProfile;
    @Setter private Location location;
    private String displayName, skinName;
    @Setter private ItemStack itemInHand, helmet, chestplate, leggings, boots;
    private Property skin;
    private Hologram hologram;
    private boolean hideName, created, tablist = true;
    private EntityArmorStand hideNameStand;
    //    private EntityPlayer entityNPC;
    private Map<Player, Boolean> players = new HashMap<>();
    private List<String> hologramLines = new ArrayList<>();
    private int unshowDistance = 45, showDistance = 32;

    public PacketNPC(JavaPlugin plugin, String displayName, Location location) {
        this.plugin = plugin;
        entityID = (int)Math.ceil(Math.random() * 1000) + 2000;
        gameProfile = new GameProfile(UUID.randomUUID(), displayName);
        this.location = location;
    }

    public PacketNPC(JavaPlugin plugin, Location location) {
        this(plugin, RandomStringUtils.randomAlphanumeric(6), location);
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

    public void setSkin(String usernameSkin, Property property) {
        this.skinName = usernameSkin;
        this.skin = property;
    }

    public void hideName() {
        hideName = true;
    }

    public boolean isNear(Player player) {
        boolean sameWorld = location.getWorld().equals(player.getWorld());
        return sameWorld && player.getLocation().distance(location) < unshowDistance;
    }

    public void removeFromTablist() {
        tablist = false;
    }

    public void update() {
        for (Player player : Bukkit.getOnlinePlayers()) if (isSpawnedTo(player)) unshow(player);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!isNear(player)) continue;
                show(player);
            }
        }, 5L);
    }

    public void show(Player player) {
        if (!created) throw new IllegalStateException("NPC not created");
        if (isSpawnedTo(player)) throw new IllegalStateException("NPC: Tried to show but is already spawned for " + player.getName());

        if (hologramLines.size() > 0) spawnHologram(player);
        if (skin != null) applySkin();

//        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityNPC), player);
//        sendPacket(new PacketPlayOutNamedEntitySpawn(entityNPC), player);
//        sendPacket(new PacketPlayOutEntity.PacketPlayOutEntityLook(entityNPC.getId(), getFixRotation(location.getYaw()), getFixRotation(location.getPitch()), true), player);

//        updateEquipment(player);
        if (hideName) hideDisplayName(player);
        if (!tablist)
//            Bukkit.getScheduler().runTaskLaterAsynchronously(zLib.getInstance(), () -> hideFromTablist(player), 20L);
        players.put(player, true);
    }

    private int getFixLocation(double pos) {
        return (int) MathHelper.floor(pos * 32.0D);
    }

    private byte getFixRotation(float angle) {
        return (byte) ((int) (angle * 256.0F / 360.0F));
    }

    public void unshow(Player player) {
        if (!created) throw new IllegalStateException("NPC: NPC not created");
        if (players.containsKey(player) && !players.get(player)) {
            throw new IllegalStateException("NPC: Tried to unshow but not spawned for " + player.getName());
        }

        if (hologramLines.size() > 0) unshowHolograms(player);
        else if (hologram != null) {
            hologram.delete();
            hologram = null;
        }
        if (hideName) sendPacket(new PacketPlayOutEntityDestroy(hideNameStand.getId()), player);

//        sendPacket(new PacketPlayOutEntityDestroy(entityNPC.getId()), player);
//        sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);

        players.put(player, false);
    }

    public void create() {
        MinecraftServer server = MinecraftServer.getServer();
        WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
        if (getSkin() != null) applySkin();

        // Old
        // entityNPC = new EntityPlayer(server, worldServer, gameProfile, new PlayerInteractManager(worldServer));
        // entityNPC.setLocation(
        //      getFixLocation(location.getX()),
        //      getFixLocation(location.getY()),
        //      getFixLocation(location.getZ()),
        //      getFixRotation(location.getYaw()),
        //      getFixRotation(location.getPitch())
        // );

        // Entity Spawn Packet
        PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
        setValue(packet, "a", entityID);
        setValue(packet, "b", gameProfile.getId());
        setValue(packet, "c", getFixLocation(location.getX()));
        setValue(packet, "d", getFixLocation(location.getY()));
        setValue(packet, "e", getFixLocation(location.getZ()));
        setValue(packet, "f", getFixRotation(location.getYaw()));
        setValue(packet, "g", getFixRotation(location.getPitch()));
        setValue(packet, "h", 0);
        DataWatcher dataWatcher = new DataWatcher(null);
        dataWatcher.a(6, (float)20);
        dataWatcher.a(10,(byte)127);
        setValue(packet,"i",dataWatcher);
        sendPacket(packet);

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

    public void equip(int slot, ItemStack itemStack) {
        PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
        setValue(packet, "a", entityID);
        setValue(packet, "b", slot);
        setValue(packet, "c", itemStack);
        sendPacket(packet);


        // Old
//        if (itemInHand != null) {
//            int entityID = entityNPC.getBukkitEntity().getEntityId();
//            sendPacket(new PacketPlayOutEntityEquipment(entityID, 0, CraftItemStack.asNMSCopy(itemInHand)), player);
//        }
//        if (helmet != null){
//            sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 4, CraftItemStack.asNMSCopy(helmet)), player);
//        }
//        if (chestplate != null) {
//            sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 3, CraftItemStack.asNMSCopy(chestplate)), player);
//        }
//        if (leggings != null) {
//            sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 2, CraftItemStack.asNMSCopy(leggings)), player);
//        }
//        if (boots != null) {
//            sendPacket(new PacketPlayOutEntityEquipment(entityNPC.getBukkitEntity().getEntityId(), 1, CraftItemStack.asNMSCopy(boots)), player);
//        }

    }

    public boolean isSpawnedTo(Player player) {
        return players.containsKey(player) && players.get(player);
    }

    // Utils methods
    private void applySkin() {
        gameProfile.getProperties().removeAll("textures");
        gameProfile.getProperties().put("textures", skin);
    }

    private void hideDisplayName(Player player) {
//        sendPacket(new PacketPlayOutSpawnEntityLiving(hideNameStand), player);
//        sendPacket(new PacketPlayOutAttachEntity(0, hideNameStand, entityID), player);
    }

    private void addToTablist(Player player) {
//        if (!tablist) {
//            Bukkit.getScheduler().runTaskLater(zLib.getInstance(), () -> {
//                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);
//            }, 10L);
//        }
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameProfile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameProfile.getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);

        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet);
        tablist = true;
    }

    private void removeFromTablist(Player player) {
//        if (!tablist) {
//            Bukkit.getScheduler().runTaskLater(zLib.getInstance(), () -> {
//                sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityNPC), player);
//            }, 10L);
//        }
        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
        PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameProfile, 1, WorldSettings.EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameProfile.getName())[0]);
        List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
        players.add(data);

        setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
        setValue(packet, "b", players);

        sendPacket(packet);
        tablist = true;
    }

    private void spawnHologram(Player player) {
        if (hologram == null) {
            Hologram hologram = new Hologram();
            hologram.setLines(hologramLines);
            hologram.setLocation(location.clone());
            hologram.create();
            hologram.show(player);
            this.hologram = hologram;
        } else {
            hologram.setLines(hologramLines);
            hologram.setLocation(location.clone());
            hologram.update(player);
        }
    }

    private void unshowHolograms(Player player) {
        if (!players.containsKey(player))
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but is not on players list.");
        if (hologram == null)
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but hologram == null");
        if (!hologram.isCreated())
            throw new IllegalStateException("NPC: Tried to unshow hologram to " + player.getName() + " but hologram is not created.");

        hologram.unshow(player);
    }

    private void deleteHolograms() {
        if (hologram != null && hologram.isCreated()) {
            hologram.delete();
        }
    }

}
