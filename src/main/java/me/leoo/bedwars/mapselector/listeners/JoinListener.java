/*
 *
 */

package me.leoo.bedwars.mapselector.listeners;

import me.leoo.bedwars.mapselector.MapSelector;
import me.leoo.bedwars.mapselector.database.Yaml;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Yaml.checkStored(player);
        MapSelector.getPlugin().getMapSelectorDatabase().checkStored(player.getUniqueId());
    }

}
