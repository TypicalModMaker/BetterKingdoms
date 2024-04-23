package dev.isnow.betterkingdoms.config.impl.database;

import io.ebean.annotation.Platform;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("database.yml")
public interface DatabaseConfig extends Config {
    default Database getDatabase() {
        return new Database("127.0.0.1", "sa", "", "betterkingdoms", Platform.H2);
    }

    default long getDatabaseAutoSaveInterval() { return 900; }
}
