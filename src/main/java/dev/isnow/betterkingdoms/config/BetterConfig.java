package dev.isnow.betterkingdoms.config;

import de.exlll.configlib.YamlConfigurations;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public abstract class BetterConfig {
    private final String name;

    public BetterConfig(String name) {
        this.name = name;
    }

    public BetterConfig load() {
        final BetterKingdoms plugin = BetterKingdoms.getInstance();

        final Path configFile = Paths.get(plugin.getDataFolder() + File.separator + name + ".yml");

        return YamlConfigurations.load(configFile, getClass());
    }

    public <T> void save() {
        final BetterKingdoms plugin = BetterKingdoms.getInstance();

        final Path configFile = Paths.get(plugin.getDataFolder() + File.separator + name + ".yml");

        YamlConfigurations.save(configFile, (Class<T>) this.getClass(), (T) this);
    }

}
