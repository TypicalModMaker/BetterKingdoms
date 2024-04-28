package dev.isnow.betterkingdoms.events;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {
        final UUID uuid = event.getUniqueId();

        BetterKingdoms.getInstance().getKingdomManager().preloadUser(uuid);

        if(!BetterKingdoms.getInstance().getKingdomManager().userExists(uuid)) {
            BetterLogger.debug("Adding new user: " + event.getName());
            BetterKingdoms.getInstance().getKingdomManager().addUser(new KingdomUser(uuid));
        }
    }
}
