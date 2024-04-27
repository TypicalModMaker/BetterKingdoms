package dev.isnow.betterkingdoms.util.cache.impl;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.util.cache.BetterCache;

public class KingdomCache extends BetterCache<String, Kingdom> {
    public KingdomCache() {
        super(key -> BetterKingdoms.getInstance().getDatabaseManager().loadKingdom(key),
              value -> BetterKingdoms.getInstance().getDatabaseManager().saveKingdom(value),
              Kingdom::deleteKingdom);
    }
}
