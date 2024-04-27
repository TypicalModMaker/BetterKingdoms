package dev.isnow.betterkingdoms.kingdoms;

import com.github.benmanes.caffeine.cache.AsyncLoadingCache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomCache;
import dev.isnow.betterkingdoms.util.cache.impl.KingdomUserCache;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class KingdomManager {

    private final KingdomCache kingdoms;

    private final KingdomUserCache kingdomUsers;

    public double nexusBlockHeight = 1;

    public KingdomManager() {
        kingdoms = new KingdomCache();

        kingdomUsers = new KingdomUserCache();
    }

    public final void addUser(final KingdomUser user) {
        kingdomUsers.put(user.getPlayerUuid(), user);

        final Kingdom attachedKingdom = user.getAttachedKingdom();

        if (attachedKingdom == null) return;

        kingdoms.put(attachedKingdom.getName(), attachedKingdom);
    }

    public final void removeUser(final KingdomUser user) {
        removeUser(user.getPlayerUuid());
    }

    public final void removeUser(final Player player) {
        removeUser(player.getUniqueId());
    }

    public final void removeUser(final UUID uuid) {

        final Optional<KingdomUser> user = findUser(uuid, false);

        if (user.isPresent()) {
            final KingdomUser kingdomUser = user.get();

            final Kingdom attachedKingdom = kingdomUser.getAttachedKingdom();
            if(attachedKingdom != null) {
                final boolean anyMemberOnline = attachedKingdom.anyMemberOnline(kingdomUser.getPlayerUuid());

                BetterLogger.debug("AnyoneOnline: " + anyMemberOnline);
                if (!anyMemberOnline) {
                    removeKingdom(attachedKingdom, false);
                }
            }
        }

        kingdomUsers.remove(uuid);
    }

    public final void addKingdom(final Kingdom kingdom) {
        kingdoms.put(kingdom.getName(), kingdom);
    }

    public final void removeKingdom(final Kingdom kingdom) {
        removeKingdom(kingdom, true);
    }

    public final void removeKingdom(final Kingdom kingdom, final boolean checkCache) {
        if (checkCache && !kingdom.anyMemberOnline(null)) return;

        kingdoms.remove(kingdom.getName());
    }

    public final Optional<KingdomUser> findUser(final Player player) {
        return findUser(player.getUniqueId(), true);
    }

    public final Optional<KingdomUser> findUser(final UUID player) {
        return findUser(player, true);
    }

    public final Optional<KingdomUser> findUser(final UUID player, final boolean fetchDB) {
        return Optional.of(kingdomUsers.get(player, fetchDB));
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName) {
        return findKingdom(kingdomName, true);
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName, final boolean fetchDB) {
        return Optional.of(kingdoms.get(kingdomName, fetchDB));
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

    public final Collection<Kingdom> getAllLoadedKingdoms() {
        return kingdoms.getAll();
    }
    public final Collection<KingdomUser> getAllLoadedUsers() {
        return kingdomUsers.getAll();
    }

}
