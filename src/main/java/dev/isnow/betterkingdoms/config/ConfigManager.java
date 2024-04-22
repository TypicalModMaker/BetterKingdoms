package dev.isnow.betterkingdoms.config;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.config.impl.commands.CommandsConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseType;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseTypeSerializer;
import lombok.Getter;
import pl.mikigal.config.Config;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

@Getter
public class ConfigManager {
    private final MasterConfig masterConfig;
    private final DatabaseConfig databaseConfig;
    private final CommandsConfig commandsConfig;

    public ConfigManager(BetterKingdoms plugin) {
        // Another api issue...
        ConfigAPI.registerSerializer(DatabaseType.class, new DatabaseTypeSerializer());

        masterConfig = (MasterConfig) init(MasterConfig.class, true, plugin);
        databaseConfig = (DatabaseConfig) init(DatabaseConfig.class, false, plugin);
        commandsConfig = (CommandsConfig) init(CommandsConfig.class, false, plugin);
    }

    private Config init(final Class<? extends Config> clazz, final boolean color, final BetterKingdoms plugin) {
        return ConfigAPI.init(
                (Class<? extends Config>) clazz,
                NameStyle.CAMEL_CASE,
                CommentStyle.ABOVE_CONTENT,
                color,
                plugin
        );
    }
}
