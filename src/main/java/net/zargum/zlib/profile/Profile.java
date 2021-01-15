package net.zargum.zlib.profile;

import net.zargum.zlib.utils.serialize.DocumentSerializable;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile implements DocumentSerializable {

    private UUID uniqueId;

    public UUID getUniqueId() {
        return this.uniqueId;
    }
    public Player asPlayer() {
        return Bukkit.getPlayer(uniqueId);
    }
    public void save() {
    }
    public void saveAsync() {
    }

    @Override
    public Document serialize() {
        return null;
    }
}
