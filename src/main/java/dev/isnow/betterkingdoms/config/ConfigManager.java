package dev.isnow.betterkingdoms.config;

import de.exlll.configlib.YamlConfigurations;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.config.impl.commands.CommandConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig;
import dev.isnow.betterkingdoms.config.impl.kingdom.KingdomConfig;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;

import java.io.File;
import java.lang.reflect.Constructor;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class ConfigManager {
    private MasterConfig masterConfig;
    private DatabaseConfig databaseConfig;
    private CommandConfig commandsConfig;
    private KingdomConfig kingdomConfig;

    public ConfigManager(final BetterKingdoms plugin) {
        load(plugin);
    }

    public void reloadConfigs() {
        load(BetterKingdoms.getInstance());
    }

    private void load(final BetterKingdoms plugin) {
        masterConfig = (MasterConfig) init(MasterConfig.class, "config", plugin);
        databaseConfig = (DatabaseConfig) init(DatabaseConfig.class, "database", plugin);
        commandsConfig = (CommandConfig) init(CommandConfig.class, "commands", plugin);
        kingdomConfig = (KingdomConfig) init(KingdomConfig.class, "kingdom", plugin);
    }

    private <T> Object init(final Class<T> clazz, final String name, final BetterKingdoms plugin) {
        final Path configFile = Paths.get(plugin.getDataFolder() + File.separator + name + ".yml");

        if (!configFile.toFile().exists()) {
            try {
                Constructor<T> constructor = clazz.getDeclaredConstructor();
                constructor.setAccessible(true);
                T newConfig = constructor.newInstance();
                YamlConfigurations.save(configFile, clazz, newConfig);
            } catch (Exception e) {
                BetterLogger.error("Failed to save default config: " + e);
            }
        }

        return YamlConfigurations.load(configFile, clazz);

    }

}
