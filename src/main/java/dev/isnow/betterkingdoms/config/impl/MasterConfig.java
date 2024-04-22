package dev.isnow.betterkingdoms.config.impl;

import dev.isnow.betterkingdoms.config.impl.database.Database;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseType;
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

    void setDebug(boolean debug);

    @Comment("Development mode [Testing purposes only]")
    default boolean getDebug() {
        return false;
    }
}
