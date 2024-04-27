package dev.isnow.betterkingdoms.events;

import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
//        ThreadUtil.saveUserAsync(event.getPlayer().getUniqueId(), true);
    }

}
