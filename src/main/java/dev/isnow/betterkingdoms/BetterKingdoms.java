package dev.isnow.betterkingdoms;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import dev.isnow.betterkingdoms.commands.CommandsManager;
import dev.isnow.betterkingdoms.config.ConfigManager;
import dev.isnow.betterkingdoms.database.DatabaseManager;
import dev.isnow.betterkingdoms.database.DatabaseRunnable;
import dev.isnow.betterkingdoms.hook.placeholderapi.BetterKingdomsExpansion;
import dev.isnow.betterkingdoms.kingdoms.KingdomManager;
import dev.isnow.betterkingdoms.reflection.ClassRegistrationManager;
import dev.isnow.betterkingdoms.util.DateUtil;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            BetterLogger.info("Registering PlaceholderAPI hook");
            new BetterKingdomsExpansion().register();
        }

        BetterLogger.info("Getting nexus block height offset...");
        final World testWorld = Bukkit.getWorlds().get(0);
        testWorld.loadChunk(0, 0, true);
        final Block block = testWorld.getHighestBlockAt(0, 0);
        block.getChunk().setForceLoaded(true);
        final Material oldMaterial = block.getType();
        block.setType(configManager.getKingdomConfig().getNexusBlock());

        final Entity testEntity = testWorld.spawnEntity(block.getLocation().clone().add(0.5, 1, 0.5), EntityType.VILLAGER);
        testEntity.setInvulnerable(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(testEntity.isDead()) {
                    BetterLogger.warn("Failed to get nexus block height, will use the default value for full solid blocks.");
                } else {
                    kingdomManager.nexusBlockHeight = -(block.getY() - testEntity.getLocation().getY());
                    BetterLogger.debug("Nexus Height: " + kingdomManager.nexusBlockHeight);

                }
                block.setType(oldMaterial);
                testEntity.remove();
                block.getChunk().setForceLoaded(false);
                testWorld.unloadChunkRequest(0, 0);
            }
        }.runTaskLater(this, 100);

        final long autoSaveInterval = configManager.getDatabaseConfig().getDatabaseAutoSaveInterval();
        new DatabaseRunnable().runTaskTimerAsynchronously(this, autoSaveInterval, autoSaveInterval);

        final String date = DateUtil.formatElapsedTime((System.currentTimeMillis() - startTime));
        BetterLogger.info("Finished loading in " + date);
    }

    @Override
    public void onDisable() {
        BetterLogger.watermark();

        BetterLogger.info("Unloading commands");
        commandsManager.unload();

        if(databaseManager.getDb() != null) {
            databaseManager.saveAllKingdoms();
            databaseManager.shutdown();
        }

        BetterLogger.info("Goodbye! :)");
    }
}
