package net.zargum.zlib.tab;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import net.zargum.zlib.textures.Texture;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;

import java.util.UUID;

@Getter @Setter
public class TablistSlot {

    private final EntityPlayer entityPlayer;
    private final String text;
    private final GameProfile profile;

    public TablistSlot(String text, Texture texture) {
        this.text = text;
        profile = new GameProfile(UUID.randomUUID(), text);
        profile.getProperties().put("textures", new Property("textures", texture.getValue()));

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldserver = server.getWorldServer(0);
        PlayerInteractManager playerinteractmanager = new PlayerInteractManager(worldserver);
        entityPlayer = new EntityPlayer(server, worldserver, profile, playerinteractmanager);
    }
    public TablistSlot(String text) {
        this(text, Texture.LIGHT_PURPLE_COLOR);
    }
}
