package net.zargum.zlib.utils;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Particles {

    public static void portal(Player player){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                EnumParticle.PORTAL,
                true,
                (float) (player.getLocation().getX()),
                (float) (player.getLocation().getY()),
                (float) (player.getLocation().getZ()),
                0,
                0,
                0,
                10,
                1000,
                null
        );
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}