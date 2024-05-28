package dev.isnow.betterkingdoms.config.impl.kingdom;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import dev.isnow.betterkingdoms.kingdoms.impl.ResourcePoint;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;

import java.util.List;

@Getter
@Setter
@Configuration
public class ResourcePointsConfig extends BetterConfig {
    @Comment({"", "How much points does specific item give?"})
    List<ResourcePoint> resourcePoints = List.of(
            new ResourcePoint(Material.DIAMOND, 50),
            new ResourcePoint(Material.EMERALD, 200)
    );

    public ResourcePointsConfig() {
        super("resource-points");
    }
}
