package dev.isnow.betterkingdoms.config.impl.database;

import pl.mikigal.config.BukkitConfiguration;
import pl.mikigal.config.serializer.Serializer;

import java.util.Locale;

public class DatabaseTypeSerializer extends Serializer<DatabaseType> {
    @Override
    protected void saveObject(final String s, final DatabaseType databaseType, final BukkitConfiguration bukkitConfiguration) {
        bukkitConfiguration.set(s, databaseType.toString().toUpperCase(Locale.ROOT));
    }

    @Override
    public DatabaseType deserialize(final String s, final BukkitConfiguration bukkitConfiguration) {
        return DatabaseType.valueOf(bukkitConfiguration.getString(s).toUpperCase(Locale.ROOT));
    }
}
