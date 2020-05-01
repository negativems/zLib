package net.zargum.zlib.proxy;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import net.zargum.zlib.zLib;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ProxyHandler implements PluginMessageListener {

    final private zLib plugin;
    private final Map<String, Integer> serverPlayersCount = new HashMap<>();
    private final Map<String, Integer> serversStatus = new HashMap<>();
    private final Map<String, Integer> serversMaxPlayers = new HashMap<>();

    public ProxyHandler(zLib plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("ServerMaxPlayers")) serversMaxPlayers.put(in.readUTF(), in.readInt());
        if (subchannel.equals("ServerStatus")) serversStatus.put(in.readUTF(), in.readInt());
        if (subchannel.equals("PlayerCount")) serverPlayersCount.put(in.readUTF(), in.readInt());
    }

}
