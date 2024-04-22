package dev.isnow.betterkingdoms.hook.placeholderapi;

import dev.isnow.betterkingdoms.BetterKingdoms;
import me.clip.placeholderapi.PlaceholderAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class BetterKingdomsExpansion extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "betterkingdoms";
    }

    @Override
    public @NotNull String getAuthor() {
        return "5170";
    }

    @Override
    public @NotNull String getVersion() {
        return BetterKingdoms.getInstance().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }


    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        params = PlaceholderAPI.setBracketPlaceholders(player, params);

        final String[] split = params.split(" ");
        if(split.length < 1) {
            return "";
        }

        final String type = split[0];
        if(type.equalsIgnoreCase("kingdom")) {

        }

        if(type.equalsIgnoreCase("user")) {

        }

        return ""; //
    }
}
