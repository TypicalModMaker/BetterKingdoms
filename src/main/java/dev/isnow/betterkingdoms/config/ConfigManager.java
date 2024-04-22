package dev.isnow.betterkingdoms.config;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.config.impl.commands.CommandsConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig;
import dev.isnow.betterkingdoms.config.impl.kingdom.KingdomConfig;
import io.ebean.annotation.Platform;
import lombok.Getter;
import pl.mikigal.config.Config;
import pl.mikigal.config.ConfigAPI;
import pl.mikigal.config.style.CommentStyle;
import pl.mikigal.config.style.NameStyle;

@Getter
public class ConfigManager {
    private MasterConfig masterConfig;
    private DatabaseConfig databaseConfig;
    private CommandsConfig commandsConfig;
    private KingdomConfig kingdomConfig;

    public ConfigManager(final BetterKingdoms plugin) {
        load(plugin);
    }

    public void reloadConfigs() {
        load(BetterKingdoms.getInstance());
    }

    private void load(final BetterKingdoms plugin) {
        masterConfig = (MasterConfig) init(MasterConfig.class, true, plugin);
        databaseConfig = (DatabaseConfig) init(DatabaseConfig.class, false, plugin);
        commandsConfig = (CommandsConfig) init(CommandsConfig.class, true, plugin);
        kingdomConfig = (KingdomConfig) init(KingdomConfig.class, true, plugin);
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
