package net.zargum.zlib.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.zargum.zlib.textures.Texture;
import net.zargum.zlib.utils.Reflections;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.UUID;

public class SkullBuilder extends ItemBuilder{

    public SkullBuilder() {
        super(new ItemStack(Material.SKULL_ITEM,1, (short) 3));
    }

    @Override
    public SkullMeta getItemMeta() {
        return (SkullMeta) item.getItemMeta();
    }

    public SkullBuilder setOwner(String name) {
        SkullMeta meta = getItemMeta();
        meta.setOwner(name);
        item.setItemMeta(meta);

        return this;
    }

    public SkullBuilder setOwner(Player player) {
        return setOwner(player.getName());
    }

    public SkullBuilder setTexture(String value) {
        SkullMeta skullMeta = (SkullMeta) item.getItemMeta();

        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", value));
        Reflections.setValue(skullMeta, "profile", profile);
        item.setItemMeta(skullMeta);

        return this;
    }

    public SkullBuilder setTexture(Texture texture) {
        return setTexture(texture.getValue());
    }
}
