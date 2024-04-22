package dev.isnow.betterkingdoms.kingdoms.impl.model;

import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "betterkingdoms_users")
@Getter@Setter
public class KingdomUser extends BaseKingdom {

    @NotNull
    private UUID playeruuid;

    @ManyToOne
    private Kingdom attachedkingdom;

    private KingdomRank kingdomRank;


    public KingdomUser(final UUID playerUUID) {
        this.playeruuid = playerUUID;
        this.attachedkingdom = null;
        this.kingdomRank = KingdomRank.MEMBER;
    }
}