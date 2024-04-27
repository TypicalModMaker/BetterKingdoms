package dev.isnow.betterkingdoms.util.cache.impl;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.cache.BetterCache;

import java.util.UUID;

public class KingdomUserCache extends BetterCache<UUID, KingdomUser> {
    public KingdomUserCache() {
        super(key -> BetterKingdoms.getInstance().getDatabaseManager().loadUser(key),
                value -> BetterKingdoms.getInstance().getDatabaseManager().saveUser(value),
                null);
    }
}
