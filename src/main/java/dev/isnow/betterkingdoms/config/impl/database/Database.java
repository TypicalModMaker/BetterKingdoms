package dev.isnow.betterkingdoms.config.impl.database;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Database implements Serializable {
    private String ip;
    private String username;
    private String password;
    private String databaseName;
    private DatabaseType databaseType;

    public Database() {} // Config system stuff
}