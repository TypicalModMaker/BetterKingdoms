package dev.isnow.betterkingdoms.config.impl.database;

import io.ebean.annotation.Platform;
import pl.mikigal.config.BukkitConfiguration;
import pl.mikigal.config.serializer.Serializer;

import java.util.Locale;

public class PlatformSerializer extends Serializer<Platform> {
    @Override
    protected void saveObject(String s, Platform platform, BukkitConfiguration bukkitConfiguration) {
        bukkitConfiguration.set(s, platform.base().toString().toUpperCase(Locale.ROOT));
    }

    @Override
    public Platform deserialize(String s, BukkitConfiguration bukkitConfiguration) {
        return Platform.valueOf(bukkitConfiguration.getString(s).toUpperCase().toUpperCase(Locale.ROOT));
    }
}
