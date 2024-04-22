package dev.isnow.betterkingdoms.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ComponentUtil;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("kingdoms|k|betterkingdoms")
@Description("Master Kingdoms Command")
@CommandPermission("betterkingdoms.main")
@SuppressWarnings("unused")
public class KingdomsCommand extends BaseCommand {

    @Subcommand("%help")
    @CatchUnknown
    @HelpCommand
    @Default
    public void doHelp(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%create")
    @CommandCompletion("%create_tab_completion")
    @CommandPermission("betterkingdoms.create")
    public void createKingdom(Player player, String kingdomName) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find your kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou already have a kingdom!"));
            return;
        }

        if(BetterKingdoms.getInstance().getKingdomManager().findKingdom(kingdomName).isPresent()) {
            player.sendMessage(ComponentUtil.deserialize("&cKingdom with a name " + kingdomName + " already exists!"));
            return;
        }

        if(player.getLocation().getBlock().getType() != Material.AIR) {
            player.sendMessage(ComponentUtil.deserialize("&cCannot place nexus in your location!"));
            return;
        }

        Kingdom kingdom = new Kingdom(kingdomName, player.getLocation());
        kingdom.addMember(kUser, KingdomRank.OWNER);

        BetterKingdoms.getInstance().getKingdomManager().addKingdom(kingdom);

        ThreadUtil.saveKingdomAsync(kingdom, null);

        player.sendMessage(ComponentUtil.deserialize("&aCreated kingdom " + kingdomName + "!"));
    }

    @Subcommand("%claim")
    @CommandPermission("betterkingdoms.claim")
    public void claimTerrain(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%disband")
    @CommandPermission("betterkingdoms.disband")
    public void disbandKingdom(Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find your kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou don't have a kingdom!"));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() != KingdomRank.OWNER) {
            player.sendMessage(ComponentUtil.deserialize("&cYou are not the owner of this kingdom!"));
            return;
        }
    }

    @Subcommand("%king")
    @CommandPermission("betterkingdoms.king")
    public void kingdomKing(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%description")
    @CommandPermission("betterkingdoms.description")
    public void kingdomDescription(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("admin %manualsave")
    @CommandPermission("betterkingdoms.admin.manualsave|betterkingdoms.admin.*")
    public void databaseSave(Player player) {
        BetterKingdoms.getInstance().getThreadPool().submit(BetterKingdoms.getInstance().getDatabaseManager()::saveAllKingdoms);
    }

    @Subcommand("admin %disband")
    @CommandCompletion("%admin_disband_tab_completion")
    @CommandPermission("betterkingdoms.admin.disband|betterkingdoms.admin.*")
    public void disbandKingdomOther(Player player, String kingdomName) {

    }


}
