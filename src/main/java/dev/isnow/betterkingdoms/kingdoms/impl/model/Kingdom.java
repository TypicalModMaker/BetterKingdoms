package dev.isnow.betterkingdoms.kingdoms.impl.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "betterkingdoms_kingdom")
@Getter@Setter
public class Kingdom extends BaseKingdom {
    @NotNull @Length(30) @Column(unique = true, name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "resourcepoints")
    private long resourcePoints;

    @NotNull @Column(name = "nexuslocation")
    private Location nexusLocation;

    @OneToMany(mappedBy = "attachedKingdom")
    private List<KingdomUser> members;

    @Transient
    private List<KingdomUser> pendingInvites = new ArrayList<>();

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

    public void deleteKingdom() {
        for(final KingdomUser user : members) {
            user.setAttachedKingdom(null);
            user.setKingdomRank(null);
        }

        for(KingdomUser user : pendingInvites) {
            user.setKingdomInvite(null);
        }

        // TODO: FIX
        nexusLocation.getBlock().setType(Material.AIR);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted") // Possible usage in future
    public boolean anyMemberOnline(UUID exception) {
        Set<UUID> onlinePlayerUuids = Bukkit.getOnlinePlayers().stream()
                .map(Player::getUniqueId)
                .collect(Collectors.toSet());

        return members.stream().anyMatch(member -> {
            UUID playerUuid = member.getPlayerUuid();
            if (playerUuid.equals(exception)) {
                return false; // Excluded member
            }

            return !onlinePlayerUuids.contains(playerUuid);
        });
    }
}