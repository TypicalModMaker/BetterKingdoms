package dev.isnow.betterkingdoms.hook.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultHook {

    public final Economy economy;
    public final EconomyResponse failHookResponse;

    public VaultHook() {
        final RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            economy = null;
            failHookResponse = null;
            return;
        }

        economy = rsp.getProvider();
        failHookResponse = new EconomyResponse(0, 0, EconomyResponse.ResponseType.SUCCESS, "");

    }

    public double getMoney(final Player player) {
        if(economy == null) {
            return 0;
        }

        return economy.getBalance(player);
    }

    public EconomyResponse withdrawMoney(final Player player, final double amount) {
        if(economy == null) {
            return failHookResponse;
        }

        return economy.withdrawPlayer(player, amount);
    }
}
