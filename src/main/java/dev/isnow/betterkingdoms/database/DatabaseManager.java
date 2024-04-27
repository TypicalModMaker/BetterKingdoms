package dev.isnow.betterkingdoms.database;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.kingdoms.impl.model.query.QKingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.query.QKingdomUser;
import dev.isnow.betterkingdoms.util.FileUtil;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.Transaction;
import io.ebean.annotation.Platform;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.dbmigration.DbMigration;
import io.ebean.migration.MigrationConfig;
import io.ebean.migration.MigrationRunner;
import lombok.Getter;
import org.apache.commons.lang3.NotImplementedException;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Getter
public class DatabaseManager {

    public static final int SCHEMA_VERSION = 10;

    private final Database db;

    public DatabaseManager(final BetterKingdoms plugin, final ClassLoader pluginLoader) {

        Database db;

        final dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig authConfig = BetterKingdoms.getInstance().getConfigManager().getDatabaseConfig();
        final DataSourceConfig dataSourceConfig = getDataSourceConfig(plugin, authConfig);

        final MasterConfig masterConfig = BetterKingdoms.getInstance().getConfigManager().getMasterConfig();
        final DatabaseConfig config = getDatabaseConfig(dataSourceConfig, masterConfig);

        final String dataPath = plugin.getDataFolder().getAbsolutePath() + File.separator + "do_not_delete_databaseMigrations";

        final boolean firstRun = masterConfig.isFirstRun();

        if(firstRun) {
            generateMigration(authConfig.getDatabaseType(), plugin);
        } else if(SCHEMA_VERSION > masterConfig.getSchemaVersion()) {
            BetterLogger.info("Schema version changed! BetterKingdoms will migrate the database automatically.");
            generateMigration(authConfig.getDatabaseType(), plugin);

            final MigrationConfig migrationConfig = new MigrationConfig();
            migrationConfig.setDbUsername(authConfig.getUsername());
            migrationConfig.setAllowErrorInRepeatable(true);
            migrationConfig.setDbPassword(authConfig.getPassword());
            migrationConfig.setDbUrl(getUrl(plugin, authConfig));
            migrationConfig.setMigrationPath("filesystem:" + dataPath);

            FileUtil.getOldestFile(new File(dataPath)).ifPresent(File::delete);

            BetterLogger.info("Running Migrator...");
            MigrationRunner runner = new MigrationRunner(migrationConfig);
            runner.run();
        }
        try {
            db = DatabaseFactory.createWithContextClassLoader(config, pluginLoader);

            // Successfully migrated
            if(!firstRun) {
                masterConfig.setSchemaVersion(SCHEMA_VERSION);
                BetterKingdoms.getInstance().getConfigManager().saveConfigs();
            }
        } catch (final Exception e) {
            e.printStackTrace();
            db = null;
        }

        this.db = db;
    }

    private void generateMigration(final Platform platform, final BetterKingdoms plugin) {
        DbMigration dbMigration = DbMigration.create();
        dbMigration.setPlatform(platform);
        dbMigration.setMigrationPath("do_not_delete_databaseMigrations");
        dbMigration.setPathToResources(plugin.getDataFolder().getAbsolutePath());
        dbMigration.setStrictMode(false);
        dbMigration.setApplyPrefix("V");
        try {
            dbMigration.generateMigration();
        } catch (IOException e) {
            BetterLogger.error("Failed to generate migration script! Contact BetterKingdoms Developer");
            e.printStackTrace();
        }
    }
    private DatabaseConfig getDatabaseConfig(final DataSourceConfig dataSourceConfig, final MasterConfig masterConfig) {
        final DatabaseConfig config = new DatabaseConfig();
        config.setDataSourceConfig(dataSourceConfig);
        config.setName("db");

        // Generate tables
        if(masterConfig.isFirstRun()) {
            config.ddlGenerate(true);
            config.ddlRun(true);

        }

        return config;
    }

    private DataSourceConfig getDataSourceConfig(final BetterKingdoms plugin, final dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig authConfig) {
        final DataSourceConfig dataSourceConfig = new DataSourceConfig();

        dataSourceConfig.setUsername(authConfig.getUsername());
        dataSourceConfig.setPassword(authConfig.getPassword());
        dataSourceConfig.setUrl(getUrl(plugin, authConfig));

        return dataSourceConfig;
    }

    private String getUrl(final BetterKingdoms plugin, final dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig authConfig) {
        switch (authConfig.getDatabaseType()) {
            case MYSQL -> {
                return "jdbc:mysql://" + authConfig.getIp() + "/" + authConfig.getDatabaseName();
            }
            case MARIADB -> {
                return "jdbc:mariadb://" + authConfig.getIp() + "/" + authConfig.getDatabaseName() + "?useLegacyDatetimeCode=false";
            }
            case H2 -> {
                return "jdbc:h2:file:" + plugin.getDataFolder().getAbsolutePath() + "/" + authConfig.getDatabaseName();
            }
            default -> throw new NotImplementedException();
        }
    }

    public void shutdown() {
        BetterLogger.info("Shutting down the database connection.");

        db.shutdown();
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

        final KingdomUser foundKingdomUser = new QKingdomUser().playerUuid.equalTo(uuid).findOne();

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
                BetterLogger.debug("Saving user: " + Bukkit.getOfflinePlayer(user.getPlayerUuid()).getName());
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

    public final void saveUser(final KingdomUser user) {
        BetterLogger.debug("Saving user " + Bukkit.getOfflinePlayer(user.getPlayerUuid()));

        db.save(user);
    }

    public final void saveKingdom(final Kingdom kingdom) {
        BetterLogger.debug("Saving kingdom " + kingdom.getName());

        db.save(kingdom);
    }

    public final void deleteKingdom(final Kingdom kingdom) {
        BetterLogger.debug("deleting kingdom " + kingdom.getName());

        kingdom.deletePermanent();
    }

}
