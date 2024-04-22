package dev.isnow.betterkingdoms.config.impl;

import dev.isnow.betterkingdoms.database.DatabaseManager;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.Comment;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("config.yml")
public interface MasterConfig extends Config {
    void setFirstRun(final boolean firstRun);

    @Comment(BetterLogger.bigPrefix + "\n DO NOT EDIT THIS LINE! Editing this will clear your database.")
    default boolean getFirstRun() {
        return true;
    }

    @Comment("DO NOT EDIT THIS LINE! Editing this will break your database.")
    default int getCurrentSchema() {
        return DatabaseManager.SCHEMA_VERSION;
    }

    void setCurrentSchema(final int currentSchema);

    @Comment("Development mode [Testing purposes only]")
    default boolean getDebug() {
        return false;
    }

    @Comment("Thread amount - Try increasing if your database is lagging with higher player count")
    default int getThreadAmount() { return 50; }
}
