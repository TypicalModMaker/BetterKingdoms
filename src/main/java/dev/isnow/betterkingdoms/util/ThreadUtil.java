package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.experimental.UtilityClass;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@UtilityClass
public class ThreadUtil {
    public void loadKingdomAsync(final String name, Consumer<Kingdom> action) {

        final CompletableFuture<Kingdom> kingdom = CompletableFuture.supplyAsync(() -> BetterKingdoms.getInstance().getDatabaseManager().loadKingdom(name), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to load kingdom: " + ex.toString());
            return null;
        });

        kingdom.thenAccept(action);
    }

    public void saveUserAsync(final UUID uuid, final boolean remove) {
        CompletableFuture.runAsync(() -> BetterKingdoms.getInstance().getThreadPool().submit(() -> BetterKingdoms.getInstance().getDatabaseManager().saveUser(uuid, remove)), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to save user: " + ex.toString());
            return null;
        });
    }

    public void saveKingdomAsync(final Kingdom kingdom, Consumer<Void> action) {

        final CompletableFuture<Void> kingdomTask = CompletableFuture.runAsync(() -> BetterKingdoms.getInstance().getDatabaseManager().saveKingdom(kingdom), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to save kingdom: " + ex.toString());
            return null;
        });

        if(action != null) {
            kingdomTask.thenAccept(action);
        }

    }

    public void loadUserAsync(final UUID uuid, Consumer<KingdomUser> action) {

        final CompletableFuture<KingdomUser> userTask = CompletableFuture.supplyAsync(() -> BetterKingdoms.getInstance().getDatabaseManager().loadUser(uuid), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to load user: " + ex.toString());
            return null;
        });

        userTask.thenAccept(action);
    }

}
