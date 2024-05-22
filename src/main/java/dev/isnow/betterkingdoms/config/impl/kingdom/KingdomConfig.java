package dev.isnow.betterkingdoms.config.impl.kingdom;

import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@Configuration
public class KingdomConfig extends BetterConfig {
    public KingdomConfig() {
        super("kingdom");
    }

    Material nexusMaterial = Material.CAMPFIRE;
    int maximumDescriptionLength = 30;

    String ownerName = "Właściciel";
    String coownerName = "Współwłaściciel";
    String officerName = "Generał";
    String knightName = "Rycerz";
    String memberName = "Członek";
}
