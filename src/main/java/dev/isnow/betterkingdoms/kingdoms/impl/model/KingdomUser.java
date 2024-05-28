package dev.isnow.betterkingdoms.kingdoms.impl.model;

import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.NotNull;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "betterkingdoms_users")
@Getter
@Setter
public class KingdomUser extends BaseKingdom {

    @NotNull
    @Column(unique = true, name = "playeruuid")
    private UUID playerUuid;

    @ManyToOne
    @Column(name = "attachedkingdom")
    private Kingdom attachedKingdom;

    @Column(name = "kingdomrank")
    private KingdomRank kingdomRank;

    @Transient
    private Kingdom kingdomInvite;

    public KingdomUser(final UUID playerUUID) {
        this.playerUuid = playerUUID;
        this.attachedKingdom = null;
        this.kingdomRank = null;
    }
}