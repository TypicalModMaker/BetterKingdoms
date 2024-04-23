package dev.isnow.betterkingdoms.commands.impl;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.config.impl.messages.MessagesConfig;
import dev.isnow.betterkingdoms.kingdoms.impl.KingdomRank;
import dev.isnow.betterkingdoms.kingdoms.impl.model.Kingdom;
import dev.isnow.betterkingdoms.kingdoms.impl.model.KingdomUser;
import dev.isnow.betterkingdoms.util.ComponentUtil;
import dev.isnow.betterkingdoms.util.ThreadUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
    public void doHelp(final Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%create")
    @CommandCompletion("%create_tab_completion")
    @CommandPermission("betterkingdoms.create")
    public void createKingdom(final Player player, final String kingdomName) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getAlreadyHasKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName()));
            return;
        }

        if(BetterKingdoms.getInstance().getKingdomManager().findKingdom(kingdomName).isPresent()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getKingdomExists(), player, "%player_name%", player.getName(), "%kingdom_name%", kingdomName));
            return;
        }

        final Location blockLocation = player.getLocation().getBlock().getLocation().clone();
        blockLocation.setPitch(player.getPitch());
        blockLocation.setYaw(player.getYaw());

        if(blockLocation.getBlock().getType() != Material.AIR) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getCantPlaceNexus(), player, "%player_name%", player.getName(), "%kingdom_name%", kingdomName, "%nexus_full_location%", ComponentUtil.formatLocation(blockLocation, true), "%nexus_short_location%", ComponentUtil.formatLocation(blockLocation, false), "%nexus_location_x", blockLocation.getBlockX(), "%nexus_location_y%", blockLocation.getBlockY(), "nexus_location_y%", blockLocation.getBlockZ()));
            return;
        }


        Kingdom kingdom = new Kingdom(kingdomName, blockLocation);
        kingdom.addMember(kUser, KingdomRank.OWNER);

        BetterKingdoms.getInstance().getKingdomManager().addKingdom(kingdom);

        ThreadUtil.saveKingdomAsync(kingdom, null);

        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getCreatedKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", kingdomName, "%nexus_full_location%", ComponentUtil.formatLocation(blockLocation, true), "%nexus_short_location%", ComponentUtil.formatLocation(blockLocation, false), "%nexus_location_x", blockLocation.getBlockX(), "%nexus_location_y%", blockLocation.getBlockY(), "nexus_location_y%", blockLocation.getBlockZ()));
    }

    @Subcommand("%claim")
    @CommandPermission("betterkingdoms.claim")
    public void claimTerrain(final Player player) {
        player.sendMessage(ComponentUtil.deserialize("&aBetterKingdoms"));
    }

    @Subcommand("%disband")
    @CommandPermission("betterkingdoms.disband")
    public void disbandKingdom(final Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        final KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        final Kingdom attachedKingdom = kUser.getAttachedKingdom();

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() != KingdomRank.OWNER) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntOwnKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName()));
            return;
        }

        ThreadUtil.deleteKingdomAsync(attachedKingdom, __ -> player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntOwnKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName())));
    }

    @Subcommand("%king")
    @CommandPermission("betterkingdoms.king")
    public void kingdomKing(final Player player, final OnlinePlayer target) {
        final Player targetPlayer = target.getPlayer();

        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);
        final Optional<KingdomUser> targetUser = BetterKingdoms.getInstance().getKingdomManager().findUser(target.getPlayer());

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        if(targetUser.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataTarget(), player, "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName()));
            return;
        }

        final KingdomUser kUser = user.get();
        final KingdomUser tUser = targetUser.get();
        final Kingdom attachedKingdom = kUser.getAttachedKingdom();

        if(kUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        if(tUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getTargetDoesntHaveKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName(), "%target_name%", targetPlayer.getName()));
            return;
        }

        if(!tUser.getAttachedKingdom().getName().equals(attachedKingdom.getName())) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getTargetInDifferentKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName(), "%target_name%", targetPlayer.getName(), "%target_kingdom_name%", tUser.getAttachedKingdom().getName()));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() != KingdomRank.OWNER) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntOwnKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName()));
            return;
        }

        if(kUser.getPlayerUuid() == tUser.getPlayerUuid()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getAlreadyOwnerOfKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName()));
            return;
        }

        tUser.setKingdomRank(KingdomRank.OWNER);
        kUser.setKingdomRank(KingdomRank.COOWNER);

        for(final KingdomUser kingdomUser : attachedKingdom.getMembers()) {
            final Player bukkitPlayer = Bukkit.getPlayer(kingdomUser.getPlayerUuid());
            if(bukkitPlayer != null) {
                player.sendMessage(ComponentUtil.deserialize(messagesConfig.getNewKingAlert(), player, "%old_player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName(), "%new_king_name%", targetPlayer.getName()));
            }
        }
    }

    @Subcommand("%description")
    @CommandPermission("betterkingdoms.description")
    @CommandCompletion("%description_tab_completion")
    public void kingdomDescription(final Player player, final String description) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        final KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        if(description.length() > BetterKingdoms.getInstance().getConfigManager().getKingdomConfig().getMaximumDescriptionLength()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDescriptionTooLong(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%kingdom_description%", description));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank().ordinal() > KingdomRank.OFFICER.ordinal()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getNoRightsToEditDescription(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%kingdom_description%", description, "%player_kingdom_rank%", ComponentUtil.getTranslationForRank(kUser.getKingdomRank())));
            return;
        }

        kUser.getAttachedKingdom().setDescription(description);
        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getChangedDescription(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%kingdom_description%", description));
    }

    @Subcommand("%invite")
    @CommandPermission("betterkingdoms.invite")
    @CommandCompletion("@players")
    public void invitePlayer(final Player player, final OnlinePlayer target) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);
        final Optional<KingdomUser> targetUser = BetterKingdoms.getInstance().getKingdomManager().findUser(target.getPlayer());

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        if(targetUser.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataTarget(), player, "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName()));
            return;
        }

        final KingdomUser kUser = user.get();
        final KingdomUser tUser = targetUser.get();
        final Kingdom attachedKingdom = kUser.getAttachedKingdom();

        if(attachedKingdom == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        if(tUser.getAttachedKingdom() != null) {
            if(attachedKingdom.getName().equals(tUser.getAttachedKingdom().getName())) {
                player.sendMessage(ComponentUtil.deserialize(messagesConfig.getAlreadyInSameKingdom(), player, "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName()));
            } else {
                player.sendMessage(ComponentUtil.deserialize(messagesConfig.getAlreadyHasKingdom(), player, "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName()));
            }
            return;
        }

        if(tUser.getKingdomInvite() != null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getTargetHasUnresolvedInvite(), player, "%player_name%", player.getName(), "%target_name%", target.getPlayer().getName()));
            return;
        }

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank() == KingdomRank.MEMBER) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getNoRightsToInvite(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%player_kingdom_rank%", ComponentUtil.getTranslationForRank(kUser.getKingdomRank())));
            return;
        }

        tUser.setKingdomInvite(kUser.getAttachedKingdom());

        attachedKingdom.getPendingInvites().add(tUser);

        final Player otherPlayer = target.getPlayer();
        otherPlayer.sendMessage(ComponentUtil.deserialize(messagesConfig.getGotInvitedMessage(), player, "%player_name%", otherPlayer.getName(), "%kingdom_name%", attachedKingdom.getName(), "%inviter_name%", player.getName()));
        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getInvitedTarget(), player, "%player_name%", player.getName(), "%kingdom_name%", attachedKingdom.getName(), "%target_name%", otherPlayer.getName()));
    }

    @Subcommand("%accept")
    @CommandPermission("betterkingdoms.accept")
    public void acceptInvite(final Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        final KingdomUser kUser = user.get();
        if(kUser.getAttachedKingdom() != null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getAlreadyHasKingdom(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName()));
            return;
        }

        if(kUser.getKingdomInvite() == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getNoPendingInvites(), player, "%player_name%", player.getName()));
            return;
        }

        final Kingdom kingdom = kUser.getKingdomInvite();
        kingdom.addMember(kUser, KingdomRank.MEMBER);

        kUser.setKingdomInvite(null);
        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getJoinedKingdom(), player, "%kingdom_name%", kUser.getAttachedKingdom().getName()));
    }

    @Subcommand("%home")
    @CommandPermission("betterkingdoms.home")
    public void teleportHome(final Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        final KingdomUser kUser = user.get();

        final Kingdom attachedKingdom = kUser.getAttachedKingdom();
        if(attachedKingdom == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        final Location homeLocation = attachedKingdom.getHomeLocation();

        player.teleport(homeLocation);

        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getTeleportedToHome(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%home_full_location%", ComponentUtil.formatLocation(homeLocation, true), "%home_short_location%", ComponentUtil.formatLocation(homeLocation, false), "%home_location_x", homeLocation.getBlockX(), "%home_location_y%", homeLocation.getBlockY(), "%home_location_y%", homeLocation.getBlockZ()));
    }

    @Subcommand("%sethome")
    @CommandPermission("betterkingdoms.sethome")
    public void setHome(final Player player) {
        final Optional<KingdomUser> user = BetterKingdoms.getInstance().getKingdomManager().findUser(player);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(user.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedDataUser(), player, "%player_name%", player.getName()));
            return;
        }

        final KingdomUser kUser = user.get();

        final Kingdom attachedKingdom = kUser.getAttachedKingdom();
        if(attachedKingdom == null) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getDoesntHaveKingdom(), player, "%player_name%", player.getName()));
            return;
        }

        final Location playerLocation = player.getLocation().clone();

        if(kUser.getKingdomRank() != null && kUser.getKingdomRank().ordinal() > KingdomRank.OFFICER.ordinal()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getNoRightsToSetHome(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%player_kingdom_rank%", ComponentUtil.getTranslationForRank(kUser.getKingdomRank()), "%home_full_location%", ComponentUtil.formatLocation(playerLocation, true), "%home_short_location%", ComponentUtil.formatLocation(playerLocation, false), "%home_location_x", playerLocation.getBlockX(), "%home_location_y%", playerLocation.getBlockY(), "%home_location_y%", playerLocation.getBlockZ()));
            return;
        }

        attachedKingdom.setHomeLocation(playerLocation);

        final String location = playerLocation.getBlockX() + ", " + playerLocation.getBlockY();
        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getSuccessfullySetNewHomeLocation(), player, "%player_name%", player.getName(), "%kingdom_name%", kUser.getAttachedKingdom().getName(), "%home_full_location%", ComponentUtil.formatLocation(playerLocation, true), "%home_short_location%", ComponentUtil.formatLocation(playerLocation, false), "%home_location_x", playerLocation.getBlockX(), "%home_location_y%", playerLocation.getBlockY(), "%home_location_y%", playerLocation.getBlockZ()));
    }

    @Subcommand("admin %manualsave")
    @CommandPermission("betterkingdoms.admin.manualsave|betterkingdoms.admin.*")
    public void databaseSave(final Player player) {
        BetterKingdoms.getInstance().getThreadPool().submit(BetterKingdoms.getInstance().getDatabaseManager()::saveAllKingdoms);
    }

    @Subcommand("admin %disband")
    @CommandCompletion("%admin_disband_tab_completion")
    @CommandPermission("betterkingdoms.admin.disband|betterkingdoms.admin.*")
    public void disbandKingdomOther(final Player player, final String kingdomName) {
        final Optional<Kingdom> kingdom = BetterKingdoms.getInstance().getKingdomManager().findKingdom(kingdomName);

        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        if(kingdom.isEmpty()) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getKingdomDoesntExist(), player, "%kingdom_name%", kingdomName));
            return;
        }

        ThreadUtil.deleteKingdomAsync(kingdom.get(), __ -> player.sendMessage(ComponentUtil.deserialize("&aKingdom successfully deleted.")));
    }

    @Subcommand("admin reload")
    @CommandPermission("betterkingdoms.admin.reload|betterkingdoms.admin.*")
    public void reloadConfig(final Player player) {
        final MessagesConfig messagesConfig = BetterKingdoms.getInstance().getConfigManager().getMessagesConfig();

        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getReloadingConfigs(), player));

        try {
            BetterKingdoms.getInstance().getConfigManager().reloadConfigs();
        } catch (Exception e) {
            player.sendMessage(ComponentUtil.deserialize(messagesConfig.getFailedToReloadConfig(), player));
            e.printStackTrace();
            return;
        }

        BetterKingdoms.getInstance().getCommandsManager().reload();

        for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.updateCommands();
        }

        player.sendMessage(ComponentUtil.deserialize(messagesConfig.getReloadedConfigs(), player));
    }
}
