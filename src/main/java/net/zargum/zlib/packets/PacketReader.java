package net.zargum.zlib.packets;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class PacketReader {

    private final Player player;
    private Channel channel;

    public PacketReader(Player player) {
        this.player = player;
    }

    public void inject() {
        CraftPlayer cPlayer = (CraftPlayer) this.player;
        channel = cPlayer.getHandle().playerConnection.networkManager.channel;
        channel.pipeline().addAfter("decoder", "PacketInjector", new MessageToMessageDecoder<Packet<?>>() {
            @Override
            protected void decode(ChannelHandlerContext arg0, Packet<?> packet, List<Object> arg2) {
                arg2.add(packet);
                readPacket(packet);
            }
        });
    }

    public void uninject() {
        if (channel.pipeline().get("PacketInjector") != null) {
            channel.pipeline().remove("PacketInjector");
            return;
        }
        throw new IllegalStateException("Packet is not injected");
    }

    public void readPacket(Packet<?> packet) {
        PacketReceiveEvent packetReceiveEvent = new PacketReceiveEvent(packet, player);
        Bukkit.getPluginManager().callEvent(packetReceiveEvent);
    }

}
