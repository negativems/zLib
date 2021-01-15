package net.zargum.zlib.skin;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.zargum.zlib.zLib;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

public class SkinUtil {

    public static String[] getProperties(String username) {
        Map<String, String[]> skins = zLib.getInstance().getSkinManager().getSkins();
        if (skins.containsKey(username)) return skins.get(username);
        if (Bukkit.getPlayer(username) != null) {
            Player player = Bukkit.getPlayer(username);
            EntityPlayer playerNMS = ((CraftPlayer) player).getHandle();
            GameProfile profile = playerNMS.getProfile();
            Property property = profile.getProperties().get("textures").iterator().next();
            String texture = property.getValue();
            String signature = property.getSignature();
            String[] properties = new String[]{texture, signature};
            skins.put(username, properties);
            return new String[]{texture, signature};
        }

        try {
            System.out.println("Searched online skin of " + username);
            URL uuidURL = new URL("https://api.mojang.com/users/profiles/minecraft/" + username);
            InputStreamReader uuidReader = new InputStreamReader(uuidURL.openStream());
            System.out.println(uuidReader.getEncoding());
            String uuid = new JsonParser().parse(uuidReader).getAsJsonObject().get("id").getAsString();

            URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            String texture = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            skins.put(username, new String[]{texture, signature});
            return new String[]{texture, signature};
        } catch (IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
            return null;
        }
    }

    public static Property getProperty(String username) {
        String[] properties = getProperties(username);
        if (properties == null) return null;
        return new Property("textures", properties[0], properties[1]);
    }

}
