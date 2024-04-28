package dev.isnow.betterkingdoms.util;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomChunk;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomChunkCache;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.experimental.UtilityClass;
import org.bukkit.Chunk;

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

    public void saveChunkAsync(final KingdomChunk chunk, Consumer<Void> action) {

        final CompletableFuture<Void> chunkTask = CompletableFuture.runAsync(() -> BetterKingdoms.getInstance().getDatabaseManager().saveChunk(chunk), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to save chunk: " + ex.toString());
            return null;
        });

        if(action != null) {
            chunkTask.thenAccept(action);
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

    public void getSpecifiedKingdomChunkAsync(final Chunk chunk, Consumer<Optional<KingdomChunk>> action) {
        final CompletableFuture<Optional<KingdomChunk>> kingdomTask = CompletableFuture.supplyAsync(() -> BetterKingdoms.getInstance().getKingdomManager().getSpecifiedKingdomChunk(chunk), BetterKingdoms.getInstance().getThreadPool()).exceptionally(ex -> {
            BetterLogger.error("Failed to get chunk: " + ex.toString());
            return null;
        });

        if(action != null) {
            kingdomTask.thenAccept(action);
        }
    }

}
