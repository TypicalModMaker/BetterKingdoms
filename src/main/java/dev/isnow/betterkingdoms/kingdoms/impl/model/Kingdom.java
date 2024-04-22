package dev.isnow.betterkingdoms.kingdoms.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "betterkingdoms_kingdom")
@Getter@Setter
public class Kingdom extends BaseKingdom {
    @NotNull @Length(30) @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "resourcepoints")
    private long resourcePoints;

    @NotNull @JsonIgnoreProperties({"pitch", "yaw"}) @Column(name = "nexuslocation")
    private Location nexusLocation;

    @OneToMany(mappedBy = "attachedKingdom")
    private List<KingdomUser> members;

    public Kingdom(final String name, final Location nexusLocation) {
        this.name = name;
        this.members = new ArrayList<>();
        this.description = "";
        this.resourcePoints = 0;
        this.nexusLocation = nexusLocation;

        nexusLocation.getBlock().setType(BetterKingdoms.getInstance().getConfigManager().getKingdomConfig().getNexusBlock());
    }

    public void addMember(final KingdomUser user, final KingdomRank rank) {
        members.add(user);
        user.setAttachedKingdom(this);
        user.setKingdomRank(rank);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Possible usage in future
    public boolean anyMemberOnline(final UUID exception) {
        return members.stream().anyMatch(kingdomUser -> {
            if(exception != null && kingdomUser.getPlayerUuid() == exception) return false;
            final Player member = Bukkit.getPlayer(kingdomUser.getPlayerUuid());
            return member == null || !member.isOnline();
        });
    }
}