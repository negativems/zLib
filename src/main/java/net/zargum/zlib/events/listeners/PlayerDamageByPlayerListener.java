package net.zargum.zlib.events.listeners;

import net.zargum.zlib.events.PlayerDamageByPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByPlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            new PlayerDamageByPlayerEvent((Player) event.getDamager(), (Player) event.getEntity(), event.getDamage());
        }
    }
}
