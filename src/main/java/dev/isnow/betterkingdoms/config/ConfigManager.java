package dev.isnow.betterkingdoms.config;

import dev.isnow.betterkingdoms.config.impl.MasterConfig;
import dev.isnow.betterkingdoms.config.impl.commands.CommandConfig;
import dev.isnow.betterkingdoms.config.impl.database.DatabaseConfig;
import dev.isnow.betterkingdoms.config.impl.kingdom.KingdomConfig;
import dev.isnow.betterkingdoms.config.impl.kingdom.ResourcePointsConfig;
import dev.isnow.betterkingdoms.config.impl.messages.MessagesConfig;
import lombok.Getter;

@Getter
public class ConfigManager {

    private MasterConfig masterConfig;
    private DatabaseConfig databaseConfig;
    private CommandConfig commandsConfig;
    private KingdomConfig kingdomConfig;
    private ResourcePointsConfig resourcePointsConfig;
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
        resourcePointsConfig = (ResourcePointsConfig) new ResourcePointsConfig().load();
        messagesConfig = (MessagesConfig) new MessagesConfig().load();
    }

    public void saveConfigs() {
        masterConfig.save();
        databaseConfig.save();
        commandsConfig.save();
        kingdomConfig.save();
        messagesConfig.save();
        resourcePointsConfig.save();
    }

}
