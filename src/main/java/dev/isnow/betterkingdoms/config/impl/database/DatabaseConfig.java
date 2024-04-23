package dev.isnow.betterkingdoms.config.impl.database;

import de.exlll.configlib.Configuration;
import io.ebean.annotation.Platform;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class DatabaseConfig {

    String ip = "127.0.0.1";
    String username = "sa";
    String password = "";
    String databaseName = "BetterKingdoms";
    Platform databaseType = Platform.H2;
    int autoSaveInterval = 900;
}
