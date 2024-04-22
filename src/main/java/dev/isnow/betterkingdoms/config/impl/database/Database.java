package dev.isnow.betterkingdoms.config.impl.database;

import lombok.Getter;
import lombok.Setter;
import pl.mikigal.config.annotation.Comment;

import java.io.Serializable;

@Getter
@Setter
public class Database implements Serializable {
    private String ip;
    private String username;
    private String password;
    private String databaseName;
    private DatabaseType databaseType;

    public Database() {} // Config system stuff

    public Database(final String ip, final String username, final String password, final String databaseName, final DatabaseType databaseType) {
        this.ip = ip;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;
        this.databaseType = databaseType;
    }
}