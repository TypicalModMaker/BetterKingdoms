package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import io.ebeaninternal.server.expression.Op;
import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

@UtilityClass
public class ThreadUtil {

    public void saveKingdomAsync(final Kingdom kingdom, Consumer<Void> action) {

        final CompletableFuture<Void> kingdomTask = CompletableFuture.runAsync(() -> BetterKingdoms.getInstance().getKingdomManager().saveKingdom(kingdom), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to save kingdom: " + ex.toString());
            return null;
        });

        if(action != null) {
            kingdomTask.thenAccept(action);
        }

    }

    public void deleteKingdomAsync(final Kingdom kingdom, Consumer<Void> action) {

        final CompletableFuture<Void> kingdomTask = CompletableFuture.runAsync(kingdom::deleteKingdom, BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to delete kingdom: " + ex.toString());
            return null;
        });

        if(action != null) {
            kingdomTask.thenAccept(action);
        }

    }

    public void getUserAsync(final UUID uuid, Consumer<Optional<KingdomUser>> action) {

        final CompletableFuture<Optional<KingdomUser>> userTask = CompletableFuture.supplyAsync(() -> BetterKingdoms.getInstance().getKingdomManager().findUser(uuid), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to get user: " + ex.toString());
            return Optional.empty();
        });

        userTask.thenAccept(action);
    }

}
