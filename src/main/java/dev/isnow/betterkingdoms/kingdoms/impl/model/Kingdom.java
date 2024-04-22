package dev.isnow.betterkingdoms.kingdoms.impl.model;

import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.base.BaseKingdom;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "betterkingdoms_kingdom")
@Getter@Setter
public class Kingdom extends BaseKingdom {
    @NotNull @Length(30)
    private String name;

    @OneToMany(mappedBy = "attachedkingdom")
    private List<KingdomUser> members;

    public Kingdom(final String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public void addMember(final KingdomUser user, final KingdomRank rank) {
        members.add(user);
        user.setAttachedkingdom(this);
        user.setKingdomRank(rank);
    }

    public boolean anyMemberOnline(final UUID exception) {
        return members.stream().anyMatch(kingdomUser -> {
            if(exception != null && kingdomUser.getPlayeruuid() == exception) return false;
            final Player member = Bukkit.getPlayer(kingdomUser.getPlayeruuid());
            return member == null || !member.isOnline();
        });
    }
}