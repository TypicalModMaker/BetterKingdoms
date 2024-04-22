package dev.isnow.betterkingdoms.config.impl.database;

import io.ebeaninternal.server.util.Str;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.ConfigName;

import java.io.Serializable;

@ConfigName("database.yml")
public interface DatabaseConfig extends Config {
    void setDatabase(Database database);

    default Database getDatabase() {
        return new Database("127.0.0.1", "sa", "", "betterkingdoms", DatabaseType.H2);
    }
}
