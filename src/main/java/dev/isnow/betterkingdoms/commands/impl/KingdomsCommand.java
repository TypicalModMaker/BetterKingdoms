package dev.isnow.betterkingdoms.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ComponentUtil;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.Bukkit;
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

        final KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou don't have a kingdom!"));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() != KingdomRank.OWNER) {
            player.sendMessage(ComponentUtil.deserialize("&cYou are not the owner of this kingdom!"));
            return;
        }

        ThreadUtil.deleteKingdomAsync(kUser.getAttachedKingdom(), __ -> player.sendMessage(ComponentUtil.deserialize("&aKingdom successfully deleted.")));
    }

    @Subcommand("%king")
    @CommandPermission("betterkingdoms.king")
    public void kingdomKing(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%description")
    @CommandPermission("betterkingdoms.description")
    @CommandCompletion("%description_tab_completion")
    public void kingdomDescription(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%invite")
    @CommandPermission("betterkingdoms.invite")
    @CommandCompletion("@players")
    public void invitePlayer(Player player, OnlinePlayer target) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);
        final Optional<KingdomUser> targetUser = BetterKingdoms.getInstance().getKingdomManager().findUser(target.getPlayer());

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find your kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        if(targetUser.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find target kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        final KingdomUser kUser = user.get();
        final KingdomUser tUser = targetUser.get();
        final Kingdom attachedKingdom = kUser.getAttachedKingdom();

        if(attachedKingdom == null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou don't have a kingdom!"));
            return;
        }

        if(tUser.getAttachedKingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize("&cThis person already has a kingdom!"));
            return;
        }

        if(tUser.getKingdomInvite() != null) {
            player.sendMessage(ComponentUtil.deserialize("&cThis person already has an invite!"));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() == KingdomRank.MEMBER) {
            player.sendMessage(ComponentUtil.deserialize("&cYou do not have rights to invite to this kingdom!"));
            return;
        }

        tUser.setKingdomInvite(kUser.getAttachedKingdom());

        attachedKingdom.getPendingInvites().add(tUser);

        final Player tPlayer = Bukkit.getPlayer(tUser.getPlayerUuid());
        if(tPlayer != null) {
            tPlayer.sendMessage(ComponentUtil.deserialize("&aYou have been invited to join " + attachedKingdom.getName() + "! Type /k accept to join"));
            player.sendMessage(ComponentUtil.deserialize("&aInvited " + tPlayer.getName()));
        } else {
            player.sendMessage(ComponentUtil.deserialize("&cInternal Error [C-01]."));
        }
    }

    @Subcommand("%accept")
    @CommandPermission("betterkingdoms.accept")
    public void acceptInvite(Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to find your kingdoms data. Contact an administrator to resolve this issue."));
            return;
        }

        final KingdomUser kUser = user.get();

        if(kUser.getAttachedKingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou already have an kingdom!"));
            return;
        }

        if(kUser.getKingdomInvite() == null) {
            player.sendMessage(ComponentUtil.deserialize("&cYou do not have any pending invites!"));
            return;
        }

        final Kingdom kingdom = kUser.getKingdomInvite();
        kingdom.addMember(kUser, KingdomRank.MEMBER);

        kUser.setKingdomInvite(null);
        player.sendMessage(ComponentUtil.deserialize("&aYou have joined " + kingdom.getName() + "!"));
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
        final Optional<Kingdom> kingdom = BetterKingdoms.getInstance().getKingdomManager().findKingdom(kingdomName);

        if(kingdom.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize("&cThis kingdom does not exist!"));
            return;
        }

        ThreadUtil.deleteKingdomAsync(kingdom.get(), __ -> player.sendMessage(ComponentUtil.deserialize("&aKingdom successfully deleted.")));
    }

    @Subcommand("admin reload")
    @CommandPermission("betterkingdoms.admin.reload|betterkingdoms.admin.*")
    public void reloadConfig(Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aReloading configs"));

        try {
            BetterKingdoms.getInstance().getConfigManager().reloadConfigs();
        } catch (Exception e) {
            player.sendMessage(ComponentUtil.deserialize("&cFailed to reload the config! Check console for more info."));
            e.printStackTrace();
            return;
        }

        BetterKingdoms.getInstance().getCommandsManager().reload();

        for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.updateCommands();
        }

        player.sendMessage(ComponentUtil.deserialize("&aSuccessfully reloaded configs!"));
    }
}
