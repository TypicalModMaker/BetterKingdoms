package dev.isnow.betterkingdoms.config.impl.kingdom;

import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@Configuration
public class KingdomConfig extends BetterConfig {
    Material nexusMaterial = Material.CAMPFIRE;
    int maximumDescriptionLength = 30;
    String ownerName = "Właściciel";
    String coownerName = "Współwłaściciel";
    String officerName = "Generał";
    String knightName = "Rycerz";
    String memberName = "Członek";
    public KingdomConfig() {
        super("kingdom");
    }
}
