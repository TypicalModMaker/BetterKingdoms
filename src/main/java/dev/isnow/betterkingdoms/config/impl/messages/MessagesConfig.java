package dev.isnow.betterkingdoms.config.impl.messages;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Material;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class MessagesConfig {

    @Comment({
            "Message Configuration File",
            "In this file you are able to modify almost every single message BetterKingdoms uses",
            "Each message supports PlaceholderAPI and custom placeholders which are defined for each message",
            "Refer to docs for more info.",
            "",
            "Placeholders:",
            "%player_name%"
    })
    String failedDataUser = "&cFailed to find your kingdoms data. Contact an administrator to resolve this issue.";

    @Comment({"",
            "Placeholders:",
            "%target_name%",
            "%player_name%"
    })
    String failedDataTarget = "&cFailed to find target's kingdoms data. Contact an administrator to resolve this issue.";

    @Comment({"",
            "Placeholders:",
            "None"
    })
    String failedToReloadConfig = "&cFailed to reload the config! Check console for more info.";

    @Comment({"",
            "Placeholders:",
            "None"
    })
    String reloadingConfigs = "&aReloading configs";

    @Comment({"",
            "Placeholders:",
            "None"
    })
    String reloadedConfigs = "&aSuccessfully reloaded configs!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
    })
    String alreadyHasKingdom = "&cYou already have a kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%"
    })
    String kingdomExists = "&cKingdom with a name %kingdom_name% already exists!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%nexus_location_x% | %nexus_location_y% | %nexus_location_z% | %nexus_short_location% | %nexus_full_location%"
    })
    String cantPlaceNexus = "&cCannot place nexus in your location!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%nexus_location_x% | %nexus_location_y% | %nexus_location_z% | %nexus_short_location% | %nexus_full_location%"
    })
    String createdKingdom = "&aCreated kingdom %kingdom_name%!";

    @Comment({"",
            "Placeholders:",
            "%player_name%"
    })
    String doesntHaveKingdom = "&cYou don't have a kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
    })
    String doesntOwnKingdom = "&cYou are not the owner of this kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
    })
    String alreadyOwnerOfKingdom = "&cYou are already the owner of this kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%"
    })
    String kingdomDeleted = "&aKingdom successfully deleted.";

    @Comment({"",
            "Placeholders:",
            "%target_name%",
            "%player_name%" // Kinda useless but whatever lol
    })
    String targetHasKingdom = "&cThis person already has a kingdom!";

    @Comment({"",
            "Placeholders:",
            "%target_name%",
            "%player_name%" // Kinda useless but whatever lol
    })
    String alreadyInSameKingdom = "&cThis person is already a member of your kingdom!";

    @Comment({"",
            "Placeholders:",
            "%target_name%",
            "%player_name%" // Kinda useless but whatever lol x2
    })
    String targetHasUnresolvedInvite = "&cThis person already has an unresolved invite!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_kingdom_rank%",
            "%player_name%"
    })
    String noRightsToInvite = "&cYou do not have rights to invite to this kingdom!";


    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%inviter_name%"
    })
    String gotInvitedMessage = "&aYou have been invited to join %kingdom_name%! Type /k accept to join";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%target_name%"
    })
    String invitedTarget = "&aInvited %target_name% to %kingdom_name%";

    @Comment({"",
            "Placeholders:",
            "%player_name%"
    })
    String noPendingInvites = "&cYou do not have any pending invites!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%"
    })
    String joinedKingdom = "&aYou have joined %kingdom_name%!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%"
    })
    String kingdomDoesntExist = "&cThis kingdom does not exist!";


    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%kingdom_description%",
            "%player_kingdom_rank%",
            "%player_name%"
    })
    String noRightsToEditDescription = "&cYou do not have rights to edit the description of your kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_description%",
            "%kingdom_name%",
            "%player_name%"
    })
    String descriptionTooLong = "&cProvided kingdom description is too long!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_description%",
            "%kingdom_name%",
            "%player_name%"
    })
    String changedDescription = "&aSuccessfully changed your kingdoms description to %description%";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%target_kingdom_name%",
            "%player_name%",
            "%target_name%"
    })
    String targetInDifferentKingdom = "&cThis person is not in your kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%target_name%"
    })
    String targetDoesntHaveKingdom = "&cThis person does not have a kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%old_king_name%",
            "%new_king_name%"
    })
    String newKingAlert = "&aPlayer %new_king_name% is now the new owner of your kingdom.";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%home_location_x% | %home_location_y% | %home_location_z% | %home_short_location% | %home_full_location%"
    })
    String teleportedToHome = "&aTeleported to your kingdoms home!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%player_kingdom_rank%",
            "%home_location_x% | %home_location_y% | %home_location_z% | %home_short_location% | %home_full_location%"
    })
    String noRightsToSetHome = "&cYou do not have rights to set the home location of your kingdom!";

    @Comment({"",
            "Placeholders:",
            "%kingdom_name%",
            "%player_name%",
            "%home_location_x% | %home_location_y% | %home_location_z% | %home_short_location% | %home_full_location%"
    })
    String successfullySetNewHomeLocation = "&aSuccessfully set your kingdoms home location to %home_location_x%, %home_location_z%";
}
