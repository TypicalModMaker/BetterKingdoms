package dev.isnow.betterkingdoms.config.impl.database;

import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import io.ebean.annotation.Platform;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class DatabaseConfig extends BetterConfig {
    String ip = "127.0.0.1";
    String username = "sa";
    String password = "";
    String databaseName = "BetterKingdoms";
    Platform databaseType = Platform.H2;
    int autoSaveInterval = 900;
    public DatabaseConfig() {
        super("database");
    }
}
