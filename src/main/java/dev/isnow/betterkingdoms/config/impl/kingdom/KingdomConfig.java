package dev.isnow.betterkingdoms.config.impl.kingdom;

import dev.isnow.betterkingdoms.config.impl.database.Database;
import io.ebean.annotation.Platform;
import org.bukkit.Material;
import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("kingdom.yml")
public interface KingdomConfig extends Config {
    default Material getNexusBlock() {
        return Material.CAMPFIRE;
    }


}