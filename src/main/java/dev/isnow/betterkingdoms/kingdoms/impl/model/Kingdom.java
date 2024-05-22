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
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "attachedKingdom", cascade = CascadeType.ALL)
    private List<KingdomChunk> claimedChunks;

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
        this.description = "";

        this.members = new ArrayList<>();

        this.resourcePoints = 0;

        this.nexusLocation = nexusLocation;
        this.homeLocation = nexusLocation.clone().add(0.5, BetterKingdoms.getInstance().getKingdomManager().nexusBlockHeight, 0.5);

        final Chunk homeChunk = homeLocation.getChunk();
        final KingdomChunk kingdomChunk = new KingdomChunk(homeChunk.getX(), homeChunk.getZ(), this);

        this.claimedChunks = new ArrayList<>();

        // this is so retarded istg

        this.save();

        kingdomChunk.save();
        this.claimedChunks.add(kingdomChunk);

        this.save();

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
}