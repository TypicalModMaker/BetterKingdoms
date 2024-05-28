package dev.isnow.betterkingdoms.database;

import dev.isnow.betterkingdoms.BetterKingdoms;
import org.bukkit.scheduler.BukkitRunnable;

public class DatabaseRunnable extends BukkitRunnable {

    @Override
    public void run() {
        if (BetterKingdoms.getInstance().getDatabaseManager().getDb() != null) {
            BetterKingdoms.getInstance().getDatabaseManager().saveAllKingdoms();
        }
    }
}
