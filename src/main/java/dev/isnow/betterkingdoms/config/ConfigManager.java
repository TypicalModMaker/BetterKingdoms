package dev.isnow.betterkingdoms.config;

import de.exlll.configlib.YamlConfigurations;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.config.impl.commands.CommandConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig;
import dev.isnow.betterkingdoms.config.impl.kingdom.KingdomConfig;
import dev.isnow.betterkingdoms.config.impl.messages.MessagesConfig;
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
    private MessagesConfig messagesConfig;

    public ConfigManager() {
        loadAll();
    }

    public void reloadConfigs() {
        loadAll();
    }

    private void loadAll() {
        masterConfig = (MasterConfig) new MasterConfig().load();
        databaseConfig = (DatabaseConfig) new DatabaseConfig().load();
        commandsConfig = (CommandConfig) new CommandConfig().load();
        kingdomConfig = (KingdomConfig) new KingdomConfig().load();
        messagesConfig = (MessagesConfig) new MessagesConfig().load();
    }

    public void saveConfigs() {
        masterConfig.save();
        databaseConfig.save();
        commandsConfig.save();
        kingdomConfig.save();
        messagesConfig.save();
    }

}
