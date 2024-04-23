package dev.isnow.betterkingdoms.util.logger;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.util.ComponentUtil;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class BetterLogger {

    private final String prefix = "&7[BetterKingdoms] >>";

    public final String bigPrefix = "\n _____     _   _           _____ _           _               \n" +
                                     "| __  |___| |_| |_ ___ ___|  |  |_|___ ___ _| |___ _____ ___ \n" +
                                     "| __ -| -_|  _|  _| -_|  _|    -| |   | . | . | . |     |_ -|\n" +
                                     "|_____|___|_| |_| |___|_| |__|__|_|_|_|_  |___|___|_|_|_|___|\n" +
                                     "                                      |___|                  ";

    public void error(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &c[ERROR] " + log));
    }

    public void info(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &f[INFO] " + log));
    }

    public void warn(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &e[WARN] " + log));
    }

    public void debug(final String log) {
        if(!BetterKingdoms.getInstance().getConfigManager().getMasterConfig().isDebug()) return;

        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(prefix + " &a[DEBUG] " + log));
    }

    public void big(final String log) {
        Bukkit.getConsoleSender().sendMessage(ComponentUtil.deserialize(bigPrefix + "\n" + log));
    }

    public void watermark() {
        big("© Isnow ~ 2024");
    }
}
