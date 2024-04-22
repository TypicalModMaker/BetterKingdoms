package dev.isnow.betterkingdoms.events;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.Objects;

public class LoginEvent implements Listener {

    @EventHandler
    public void onLogin(AsyncPlayerPreLoginEvent event) {

        // Doesn't need to be async if the event is async, but I want to use our dedicated pool for that
        ThreadUtil.loadUserAsync(event.getUniqueId(), kingdomUser -> BetterKingdoms.getInstance().getKingdomManager().addUser(Objects.requireNonNullElseGet(kingdomUser, () -> new KingdomUser(event.getUniqueId()))));
    }
}
