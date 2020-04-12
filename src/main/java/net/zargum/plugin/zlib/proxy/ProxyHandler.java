package net.zargum.plugin.zlib.proxy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.zargum.plugin.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.ChannelNotRegisteredException;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.*;

@Getter
public class ProxyHandler implements PluginMessageListener {

    final private zLib plugin;
    private List<String> servers = new ArrayList<>();
    private Map<String, Integer> serversCount = new HashMap<>();
    private Map<String, Integer> serversStatus = new HashMap<>(); // 0 = offline, 1 = whitelisted, 2 = online
    private Map<String, Integer> serversMaxPlayers = new HashMap<>();
    private Map<String, Boolean> whitelistStatus = new HashMap<>();

    public ProxyHandler(zLib plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) serversCount.put(in.readUTF(), in.readInt());
        if (subchannel.equals("ServerStatus")) serversStatus.put(in.readUTF(), in.readInt());
        if (subchannel.equals("ServerMaxPlayers")) serversMaxPlayers.put(in.readUTF(), in.readInt());
        if (subchannel.equals("WhitelistStatus")) whitelistStatus.put(in.readUTF(), in.readBoolean());
        if (subchannel.equals("GetServers")) servers = Arrays.asList(in.readUTF().split(", "));
    }

    public void loadServers() {
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            try {
                out.writeUTF("GetServers");
            } catch (Exception e) {
                e.printStackTrace();
            }
            plugin.getServer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
        } catch (ChannelNotRegisteredException e) {
            Bukkit.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
        }
    }

}
