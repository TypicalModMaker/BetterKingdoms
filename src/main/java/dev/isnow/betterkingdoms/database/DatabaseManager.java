package dev.isnow.betterkingdoms.database;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseType;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.kingdoms.impl.model.query.QKingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.query.QKingdomUser;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.Query;
import io.ebean.Transaction;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebeaninternal.server.util.Str;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;
import org.checkerframework.checker.units.qual.K;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@Getter
public class DatabaseManager {

    private final Database db;

    public DatabaseManager(final BetterKingdoms plugin, final ClassLoader pluginLoader) {

        Database db = null;

        final DataSourceConfig dataSourceConfig = getDataSourceConfig(plugin);

        final DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);
        config.setName("db");

        // Generate tables
        if(BetterKingdoms.getInstance().getConfigManager().getMasterConfig().getFirstRun()) {
            config.ddlGenerate(true);
            config.ddlRun(true);
        }

        // Test Connection
        try {
            db = DatabaseFactory.createWithContextClassLoader(config, pluginLoader);

            final Kingdom schemaKingdom = new Kingdom("BetterKingdoms");
            schemaKingdom.save();

            final KingdomUser schemaUser = new KingdomUser(UUID.fromString("00000000-0000-0000-0000-000000000000"));
            schemaUser.save();
        } catch (final Exception e) {
            // Table generation script
            if(!e.toString().contains("Failed to run script")) {
                e.printStackTrace();
            } else {
                db = null;
            }
        }

        this.db = db;
    }

    private DataSourceConfig getDataSourceConfig(BetterKingdoms plugin) {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dev.isnow.betterkingdoms.config.impl.database.Database authConfig = BetterKingdoms.getInstance().getConfigManager().getDatabaseConfig().getDatabase();

        dataSourceConfig.setUsername(authConfig.getUsername());
        dataSourceConfig.setPassword(authConfig.getPassword());
        switch (authConfig.getDatabaseType()) {
            case MYSQL -> dataSourceConfig.setUrl("jdbc:mysql://" + authConfig.getIp() + "/" + authConfig.getDatabaseName());
            case MARIADB -> dataSourceConfig.setUrl("jdbc:mariadb://" + authConfig.getIp() + "/" + authConfig.getDatabaseName() + "?useLegacyDatetimeCode=false");
            case H2 -> dataSourceConfig.setUrl("jdbc:h2:file:" + plugin.getDataFolder().getAbsolutePath() + "/" + authConfig.getDatabaseName());
            default -> throw new NotImplementedException();
        }
        return dataSourceConfig;
    }

    public final Kingdom loadKingdom(final String name) {
        BetterLogger.debug("Loading kingdom " + name);

        final Kingdom foundKingdom = new QKingdom().name.equalTo(name).findOne();

        if (foundKingdom == null) {
            BetterLogger.debug("Tried to a load non-existing kingdom with name " + name);
        }

        return foundKingdom;
    }

    public final KingdomUser loadUser(final UUID uuid) {
        BetterLogger.debug("Loading user " + uuid);

        final KingdomUser foundKingdomUser = new QKingdomUser().playeruuid.equalTo(uuid).findOne();

        if (foundKingdomUser == null) {
            BetterLogger.debug("Tried to load an non-existing user with name " + Bukkit.getOfflinePlayer(uuid).getName());
        }

        return foundKingdomUser;
    }


    public final void saveAllKingdoms() {
        BetterLogger.info("Saving all data to the database...");

        final Transaction transaction = db.beginTransaction();

        try {
            for(final Kingdom kingdom : BetterKingdoms.getInstance().getKingdomManager().getAllLoadedKingdoms()) {
                BetterLogger.debug("Saving kingdom: " + kingdom.getName());
                kingdom.save();
            }

            for(final KingdomUser user : BetterKingdoms.getInstance().getKingdomManager().getAllLoadedUsers()) {
                BetterLogger.debug("Saving user: " + Bukkit.getOfflinePlayer(user.getPlayeruuid()).getName());
                user.save();
            }
            transaction.commit();
        } catch (final Exception e) {
            BetterLogger.error("Failed to save user data to the database! Error: " + e);
            transaction.rollback();
        } finally {
            transaction.end();
        }
    }

    public final void saveUser(final UUID uuid) {
        saveUser(uuid, true);
    }

    public final void saveUser(final UUID uuid, final boolean remove) {
        Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(uuid, false);

        BetterLogger.debug("Saving user " + uuid);

        if(user.isPresent()) {
            user.get().save();
        } else {
            BetterLogger.debug("Tried to save a non-existing user");
        }

        if(remove) {
            BetterKingdoms.getInstance().getKingdomManager().removeUser(uuid);
        }
    }

    public final void saveKingdom(final Kingdom kingdom) {
        BetterLogger.debug("Saving kingdom " + kingdom.getName());

        db.save(kingdom);
    }

    public void shutdown() {
        BetterLogger.info("Shutting down the database connection.");

        db.shutdown();
    }

}
