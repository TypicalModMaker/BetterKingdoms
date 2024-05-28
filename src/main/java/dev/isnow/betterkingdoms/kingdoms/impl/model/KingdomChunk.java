package dev.isnow.betterkingdoms.kingdoms.impl.model;

import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "betterkingdoms_chunk")
@Getter
@Setter
public class KingdomChunk extends BaseKingdom {
    @Column(name = "chunkx")
    private int chunkX;

    @Column(name = "chunkz")
    private int chunkZ;

    @ManyToOne
    @Column(name = "attachedkingdom")
    @NotNull
    private Kingdom attachedKingdom;

    public KingdomChunk(final int chunkX, final int chunkZ, final Kingdom attachedKingdom) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.attachedKingdom = attachedKingdom;

    }
}
