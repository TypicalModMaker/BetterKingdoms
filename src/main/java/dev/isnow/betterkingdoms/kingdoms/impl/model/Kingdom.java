package dev.isnow.betterkingdoms.kingdoms.impl.model;

import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import dev.isnow.betterkingdoms.util.converter.AdvancedLocationConverter;
import dev.isnow.betterkingdoms.util.converter.LocationConverter;
import dev.isnow.betterkingdoms.util.logger.BetterLogger;
import io.ebean.Transaction;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

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

    @NotNull @Column(name = "nexuslocation") @Convert(converter = LocationConverter.class)
    private Location nexusLocation;

    @NotNull @Column(name = "homelocation") @Convert(converter = AdvancedLocationConverter.class)
    private Location homeLocation;

    @OneToMany(mappedBy = "attachedKingdom", cascade = CascadeType.ALL)
    private List<KingdomUser> members;

    @Transient
    private List<KingdomUser> pendingInvites = new ArrayList<>();

    public Kingdom(final String name, final Location nexusLocation) {
        this.name = name;
        this.members = new ArrayList<>();
        this.description = "";
        this.resourcePoints = 0;
        this.nexusLocation = nexusLocation;
        this.homeLocation = nexusLocation.clone().add(0.5, BetterKingdoms.getInstance().getKingdomManager().nexusBlockHeight, 1.5);

        nexusLocation.getBlock().setType(BetterKingdoms.getInstance().getConfigManager().getKingdomConfig().getNexusMaterial());
    }

    public void addMember(final KingdomUser user, final KingdomRank rank) {
        members.add(user);
        user.setAttachedKingdom(this);
        user.setKingdomRank(rank);
    }

    public void deleteKingdom() {
        final Transaction transaction = BetterKingdoms.getInstance().getDatabaseManager().getDb().beginTransaction();

        try {
            for(final KingdomUser user : members) {
                user.setAttachedKingdom(null);
                user.setKingdomRank(null);
                user.save();
            }

            for(final KingdomUser user : pendingInvites) {
                user.setKingdomInvite(null);
                user.save();
            }

            transaction.commit();
        } catch (final Exception e) {
            BetterLogger.error("Failed to save user data to the database! Error: " + e);
            transaction.rollback();
        } finally {
            transaction.end();
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                nexusLocation.getBlock().setType(Material.AIR);
            }

        }.runTask(BetterKingdoms.getInstance());

        BetterKingdoms.getInstance().getDatabaseManager().deleteKingdom(this);
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

            return onlinePlayerUuids.contains(playerUuid);
        });
    }
}