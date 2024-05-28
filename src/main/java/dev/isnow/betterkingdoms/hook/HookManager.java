package dev.isnow.betterkingdoms.hook;

import dev.isnow.betterkingdoms.hook.placeholderapi.BetterKingdomsExpansion;
import dev.isnow.betterkingdoms.hook.vault.VaultHook;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import lombok.Getter;
import org.bukkit.Bukkit;

@Getter
public class HookManager {

    private final boolean placeholerAPIHook;
    private VaultHook vaultHook;

    public HookManager() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") != null) {
            BetterLogger.info("Hooking into Vault");
            vaultHook = new VaultHook();
        }

        placeholerAPIHook = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (placeholerAPIHook) {
            BetterLogger.info("Hooking into PlaceholderAPI");
            new BetterKingdomsExpansion().register();
        }

    }
}
