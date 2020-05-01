package net.zargum.zlib.lunar.event.impl;

import net.zargum.zlib.lunar.event.PlayerEvent;
import org.bukkit.entity.Player;

public class AuthenticateEvent extends PlayerEvent {

    public AuthenticateEvent(Player player) {
        super(player);
    }

}
