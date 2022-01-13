package net.zargum.zlib.tab;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerListHeaderFooter;
import net.zargum.zlib.textures.Texture;
import net.zargum.zlib.utils.Reflections;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class Tablist {

    private final Map<Integer, TablistSlot> slots = new ConcurrentHashMap<>();
    private final int rows;
    private String header, footer;

    public Tablist(int rows) {
        this.rows = rows;
    }

    private void initialize() {
        for (int i = 0; i < rows*20; i++) {
            slots.put(i, new TablistSlot(""));
        }
    }

    public Tablist setSlot(int slot, String text) {
        slots.put(slot, new TablistSlot(text));
        return this;
    }
    public Tablist setSlot(int slot, String text, Texture texture) {
        slots.put(slot, new TablistSlot(text, texture));
        return this;
    }

    private void clear(Player player) {
        Collection<? extends Player> playersBukkit = Bukkit.getOnlinePlayers();
        EntityPlayer[] playersNMS = new EntityPlayer[playersBukkit.size()];
        int current = 0;
        for (Player online : playersBukkit) {
            playersNMS[current] = ((CraftPlayer) online).getHandle();
            current++;
        }

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, playersNMS);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public Tablist setHeader(String text) {
        this.header = text;
        return this;
    }

    public Tablist setFooter(String text) {
        this.footer = text;
        return this;
    }

    public void updateHeaderAndFooter(Player player) {
        IChatBaseComponent tabTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + header+ "\"}");
        IChatBaseComponent tabSubTitle = IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + footer + "\"}");

        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter(tabTitle);
        Reflections.setValue(packet, "b", tabSubTitle);
        Reflections.sendPacket(packet, player);
    }

    public void sendTo(Player player) {
        updateHeaderAndFooter(player);

        List<EntityPlayer> entityPlayers = new ArrayList<>();
        for (TablistSlot slot : slots.values()) {
            entityPlayers.add(slot.getEntityPlayer());
        }

        PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayers);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
