package dev.isnow.betterkingdoms.config.impl;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class MasterConfig extends BetterConfig {
    public MasterConfig() {
        super("config");
    }

    @Comment({BetterLogger.bigPrefix, " ", "BetterKingdoms Configuration, refer to the docs for more info.", "", "DO NOT TOUCH! This will drop your database."})
    boolean firstRun = true;

    @Comment({"", "Development messages in console, useful for debugging and fixing issues."})
    boolean debug = false;

    @Comment({"", "DO NOT TOUCH! This might break your database or make BetterKingdoms not being able to load."})
    int schemaVersion = 10;

    @Comment({"", "Increase this if your database is lagging with higher player count"})
    int threadAmount = 50;

    @Comment({"", "Increase this if your database is being called too often"})
    int cacheSizeLimit = 500;
}
