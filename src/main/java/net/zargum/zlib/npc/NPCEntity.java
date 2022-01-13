package net.zargum.zlib.npc;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

public class NPCEntity extends EntityPlayer {
    public NPCEntity(MinecraftServer server, WorldServer world, GameProfile profile) {
        super(server, world, profile, new PlayerInteractManager(world));
    }
}
