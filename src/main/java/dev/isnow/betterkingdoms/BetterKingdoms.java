package dev.isnow.betterkingdoms;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.betterkingdoms.commands.CommandsManager;
import dev.isnow.betterkingdoms.config.ConfigManager;
import dev.isnow.betterkingdoms.database.DatabaseManager;
import dev.isnow.betterkingdoms.kingdoms.KingdomManager;
import dev.isnow.betterkingdoms.reflection.ClassRegistrationManager;
import dev.isnow.betterkingdoms.util.DateUtil;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public final class BetterKingdoms extends JavaPlugin {

    @Getter
    private static BetterKingdoms instance;

    private CommandsManager commandsManager;
    private DatabaseManager databaseManager;
    private ConfigManager configManager;

    private final KingdomManager kingdomManager = new KingdomManager();

    private ExecutorService threadPool;

    @Override
    public void onEnable() {
        final long startTime = System.currentTimeMillis();

        instance = this;
        BetterLogger.watermark();

        BetterLogger.info("Initializating config");
        configManager = new ConfigManager(this);

        threadPool = Executors.newFixedThreadPool(configManager.getMasterConfig().getThreadAmount(), new ThreadFactoryBuilder().setNameFormat("betterkingdoms-worker-thread-%d").build());

        BetterLogger.info("Initializating events");
        ClassRegistrationManager.loadListeners("dev.isnow.betterkingdoms.events");

        BetterLogger.info("Initializating commands");
        commandsManager = new CommandsManager(this);

        BetterLogger.info("Initializating database connection");
        final ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
        final ClassLoader pluginClassLoader = BetterKingdoms.class.getClassLoader();

        Thread.currentThread().setContextClassLoader(pluginClassLoader);

        databaseManager = new DatabaseManager(this, pluginClassLoader);

        Thread.currentThread().setContextClassLoader(originalClassLoader);

        if(databaseManager.getDb() == null) {
            BetterLogger.info("Failed to connect to the database! This plugin won't work without an database. Refer to docs for more info.");
            Bukkit.getPluginManager().disablePlugin(BetterKingdoms.getInstance());
            return;
        } else {
            BetterLogger.info("Connected successfully.");
            configManager.getMasterConfig().setFirstRun(false);
        }

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        BetterLogger.info("Finished loading in " + date);
    }

    @Override
    public void onDisable() {
        BetterLogger.watermark();

        if(databaseManager.getDb() != null) {
            databaseManager.saveAllKingdoms();
            databaseManager.shutdown();
        }

        BetterLogger.info("Goodbye! :)");
    }
}
