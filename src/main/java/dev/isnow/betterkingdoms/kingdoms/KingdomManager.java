package dev.isnow.betterkingdoms.kingdoms;

import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomChunk;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomCache;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomChunkCache;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomUserCache;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class KingdomManager {

    private final KingdomCache kingdoms;

    private final KingdomUserCache kingdomUsers;

    private final KingdomChunkCache kingdomChunks;

    public double nexusBlockHeight = 1;

    public KingdomManager() {
        kingdoms = new KingdomCache();

        kingdomUsers = new KingdomUserCache();

        kingdomChunks = new KingdomChunkCache();
    }

    public final void addKingdom(final Kingdom kingdom) {
        kingdoms.put(kingdom.getName(), kingdom);
    }

    public final void addUser(final KingdomUser user) {
        kingdomUsers.put(user.getPlayerUuid(), user);
    }

    public Optional<KingdomChunk> getSpecifiedKingdomChunk(final Chunk chunk) {
        return Optional.ofNullable(kingdomChunks.get(chunk));
    }

    public final Optional<KingdomUser> findUser(final Player player) {
        return findUser(player.getUniqueId(), true);
    }

    public final Optional<KingdomUser> findUser(final UUID player) {
        return findUser(player, true);
    }

    public final Optional<KingdomUser> findUser(final UUID player, final boolean fetchDB) {
        return Optional.ofNullable(kingdomUsers.get(player, fetchDB));
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName) {
        return findKingdom(kingdomName, true);
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName, final boolean fetchDB) {
        return Optional.ofNullable(kingdoms.get(kingdomName, fetchDB));
    }

    public final void deleteKingdom(final Kingdom kingdom) {
        kingdoms.remove(kingdom.getName());
    }

    public final void saveKingdom(final Kingdom kingdom) {
        kingdoms.save(kingdom);
    }

    public final void preloadUser(final UUID uuid) {
        kingdomUsers.preload(uuid);
    }

    public final boolean userExists(final UUID uuid) {
        return kingdomUsers.contains(uuid);
    }

    public final Collection<Kingdom> getAllLoadedKingdoms() {
        return kingdoms.getAll();
    }

    public final Collection<KingdomUser> getAllLoadedUsers() {
        return kingdomUsers.getAll();
    }

}
