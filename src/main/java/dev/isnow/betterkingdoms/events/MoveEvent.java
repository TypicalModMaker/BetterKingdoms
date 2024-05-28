package dev.isnow.betterkingdoms.events;

import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        if ((from.getBlockX() >> 4) != (to.getBlockX() >> 4) || (from.getBlockZ() >> 4) != (to.getBlockZ() >> 4)) {
            ThreadUtil.getSpecifiedKingdomChunkAsync(event.getTo().getChunk(), kingdom -> {
                // TODO: Switch to some non-deprecated method
                kingdom.ifPresent(kingdomChunk -> event.getPlayer().sendTitle("Entered kingdom:", kingdomChunk.getAttachedKingdom().getName(), 15, 40, 15));
            });
        }
    }
}
