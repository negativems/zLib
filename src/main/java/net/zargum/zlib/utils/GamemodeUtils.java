package net.zargum.zlib.utils;

import org.bukkit.GameMode;

import java.util.Arrays;
import java.util.HashMap;

public class GamemodeUtils {

    private static HashMap<String, String[]> gamemodes = new HashMap<String, String[]>() {{
        put("SURVIVAL", new String[]{"survival","s","0"});
        put("CREATIVE", new String[]{"creative","c","1"});
        put("ADVENTURE", new String[]{"adventure","a","2"});
        put("SPECTATOR", new String[]{"spectator","sp","3"});
    }};

    public static boolean isGamemode(String gm) {
        for (String[] gamemode : gamemodes.values()) {
            if (Arrays.asList(gamemode).contains(gm.toLowerCase())) return true;
        }
        return false;
    }

    public static String getName(String gm) {
        for (String[] gamemode : gamemodes.values()) {
            if (Arrays.asList(gamemode).contains(gm.toLowerCase())) return gamemode[0];
        }
        return null;
    }

    public static GameMode getGamemode(String gm) {
        for (String[] gamemode : gamemodes.values()) {
            if (Arrays.asList(gamemode).contains(gm.toLowerCase())) return GameMode.valueOf(gamemode[0].toUpperCase());
        }
        return null;
    }
}
