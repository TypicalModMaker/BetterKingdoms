package dev.isnow.betterkingdoms.kingdoms;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import org.bukkit.entity.Player;

import java.util.*;

public class KingdomManager {

    private final Map<String, Kingdom> kingdoms = new HashMap<>();

    private final Map<UUID, KingdomUser> kingdomUsers = new HashMap<>();

    public final void addUser(final KingdomUser user) {
        kingdomUsers.put(user.getPlayerUuid(), user);

        final Kingdom attachedKingdom = user.getAttachedKingdom();

        if (attachedKingdom == null) return;

        if (!kingdoms.containsKey(attachedKingdom.getName())) {
            BetterLogger.debug("Adding " + attachedKingdom.getName() + " to the cache kingdoms list");
            kingdoms.put(attachedKingdom.getName(), attachedKingdom);
        } else {
            user.setAttachedKingdom(findKingdom(attachedKingdom.getName(), false).orElse(attachedKingdom));
        }
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
            if (attachedKingdom != null && !attachedKingdom.anyMemberOnline(kingdomUser.getPlayerUuid())) {
                removeKingdom(attachedKingdom, false);
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

        ThreadUtil.saveKingdomAsync(kingdom, __ -> {
            kingdoms.remove(kingdom.getName());
        });
    }

    public final Optional<KingdomUser> findUser(final Player player) {
        return findUser(player.getUniqueId(), true);
    }

    public final Optional<KingdomUser> findUser(final UUID player) {
        return findUser(player, true);
    }

    public final Optional<KingdomUser> findUser(final UUID player, final boolean fetchDB) {
        Optional<KingdomUser> kingdomUser = Optional.ofNullable(kingdomUsers.get(player));

        if (kingdomUser.isEmpty() && fetchDB) {
            KingdomUser sqlKingdomUser = BetterKingdoms.getInstance().getDatabaseManager().loadUser(player);
            if (sqlKingdomUser != null) {
                kingdomUsers.put(sqlKingdomUser.getPlayerUuid(), sqlKingdomUser);

                kingdomUser = Optional.of(sqlKingdomUser);
            }
        }

        return kingdomUser;
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName) {
        return findKingdom(kingdomName, true);
    }

    public final Optional<Kingdom> findKingdom(final String kingdomName, final boolean fetchDB) {
        Optional<Kingdom> kingdom = Optional.ofNullable(kingdoms.get(kingdomName));

        if (kingdom.isEmpty() && fetchDB) {
            final Kingdom sqlKingdom = BetterKingdoms.getInstance().getDatabaseManager().loadKingdom(kingdomName);
            if (sqlKingdom != null) {
                kingdoms.put(sqlKingdom.getName(), sqlKingdom);

                kingdom = Optional.of(sqlKingdom);
            }
        }

        return kingdom;
    }

    public final Collection<Kingdom> getAllLoadedKingdoms() {
        return kingdoms.values();
    }
    public final Collection<KingdomUser> getAllLoadedUsers() {
        return kingdomUsers.values();
    }

}
