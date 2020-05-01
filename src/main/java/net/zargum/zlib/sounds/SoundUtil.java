package net.zargum.zlib.sounds;

import org.bukkit.entity.Player;

public class SoundUtil {

    public static void play(Player player, Sounds sounds) {
        player.playSound(player.getLocation(),sounds.getSound(), 0.5F, sounds.getPitch());
    }

}
