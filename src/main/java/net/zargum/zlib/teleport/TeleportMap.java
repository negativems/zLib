package net.zargum.zlib.teleport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Location;

@Getter
@AllArgsConstructor
public class TeleportMap {

    private final Location from;
    private final Location to;

}
