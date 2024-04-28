package dev.isnow.betterkingdoms.util.cache.impl;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomChunk;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.cache.BetterCache;
import org.bukkit.Chunk;

import java.util.UUID;

public class KingdomChunkCache extends BetterCache<Chunk, KingdomChunk> {
    public KingdomChunkCache() {
        super("Chunk",
                key -> BetterKingdoms.getInstance().getDatabaseManager().loadChunk(key),
                value -> BetterKingdoms.getInstance().getDatabaseManager().saveChunk(value),
                null);
    }
}
