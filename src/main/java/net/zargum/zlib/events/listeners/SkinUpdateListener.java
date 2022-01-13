package net.zargum.zlib.events.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.zargum.zlib.skin.SkinManager;
import net.zargum.zlib.zLib;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class SkinUpdateListener implements Listener {

    private final zLib plugin;

    public SkinUpdateListener(zLib plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        SkinManager skinManager = plugin.getSkinManager();
        if (skinManager.getSkins().containsKey(player.getName())) {
            GameProfile profile = ((CraftPlayer) player).getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();
            String[] properties = new String[]{texture, signature};
            skinManager.getSkins().put(player.getName(), properties);
        }
    }

}
