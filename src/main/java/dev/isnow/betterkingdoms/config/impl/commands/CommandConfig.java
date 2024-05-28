package dev.isnow.betterkingdoms.config.impl.commands;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import dev.isnow.betterkingdoms.util.type.Locale;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class CommandConfig extends BetterConfig {

    @Comment({"", "Available locales are listed on the wiki"})
    Locale commandsLocale = Locale.POLISH;

    String helpMessage = """
            &aBetterKingdoms Help\s
            /k help - Help Command\s
            /k create <Kingdom_Name> - Create kingdom\s
            /k claim - Claim chunk \s
            /k disband - Disband your kingdom\s
            /k king <Player> - Switch your kingdom ownership\s
            /k accept - Accept invite to a kingdom\s
            /k invite <Player> - Invite a player to your kingdom\s
            /k description <Description> - Set the description of your kingdom\s
            /k home - Teleport to your kingdom home point\s
            /k sethome - Set the home point of your kingdom\s
            /k leave - Leave your kingdom\s
            """;

    String help = "pomoc|help";
    String create = "zaloz|stworz|create", create_tab_completion = "<Nazwa_Królestwa>";
    String claim = "zajmij|claim";
    String disband = "rozwiaz|usun|disband|delete", disband_admin_tab_completion = "<Nazwa_Królestwa>";
    String king = "krol|king";
    String accept = "akceptuj|accept";
    String invite = "zapros|invite";
    String description = "opis|description|lore", description_tab_completion = "<Opis_Królestwa>";
    String home = "dom|home";
    String sethome = "ustawdom|sethome";
    String leave = "opusc|leave";

    public CommandConfig() {
        super("commands");
    }
}
