package dev.isnow.betterkingdoms.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ComponentUtil;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.entity.Player;

import java.util.Optional;

@CommandAlias("kingdoms|k|betterkingdoms")
@Description("Master Kingdoms Command")
@CommandPermission("betterkingdoms.main")
@SuppressWarnings("unused")
public class KingdomsCommand extends BaseCommand {

    @Subcommand("help")
    @CatchUnknown
    @HelpCommand
    @Default
    public void doHelp(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("create|zaloz")
    @Syntax("<name>")
    @CommandPermission("betterkingdoms.create")
    public void createKingdom(Player player, String kingdomName) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find your kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        KingdomUser kUser = user.get();
        if(kUser.getAttachedkingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou already have a kingdom!"));
            return;
        }

        if(BetterKingdoms.getInstance().getKingdomManager().findKingdom(kingdomName).isPresent()) {
            player.sendMessage(ComponentUtil.deserialize("&cKingdom with a name " + kingdomName + " already exists!"));
            return;
        }

        Kingdom kingdom = new Kingdom(kingdomName);
        kingdom.addMember(kUser, KingdomRank.OWNER);

        BetterKingdoms.getInstance().getKingdomManager().addKingdom(kingdom);

        ThreadUtil.saveKingdomAsync(kingdom, null);

        player.sendMessage(ComponentUtil.deserialize("&aCreated kingdom " + kingdomName + "!"));
    }

    @Subcommand("zajmij|claim")
    @CommandPermission("betterkingdoms.claim")
    public void claimTerrain(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("porzuc|abandon")
    @CommandPermission("betterkingdoms.abandon")
    public void abandonKingdom(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("krol|king")
    @CommandPermission("betterkingdoms.king")
    public void kingdomKing(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("opis|lore|description")
    @CommandPermission("betterkingdoms.description")
    public void kingdomDescription(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("manualsave")
    @CommandPermission("betterkingdoms.admin.manualsave")
    public void databaseSave(Player player) {
        BetterKingdoms.getInstance().getThreadPool().submit(BetterKingdoms.getInstance().getDatabaseManager()::saveAllKingdoms);
    }


}
