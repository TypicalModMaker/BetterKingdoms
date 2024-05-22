package dev.isnow.betterkingdoms.config.impl.commands;

import co.aikar.commands.Locales;
import de.exlll.configlib.Configuration;
import dev.isnow.betterkingdoms.config.BetterConfig;
import dev.isnow.betterkingdoms.util.type.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Configuration
public class CommandConfig extends BetterConfig {

    public CommandConfig() {
        super("commands");
    }

    Locale commandsLocale = Locale.POLISH;

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
    String manualsave = "manualsave";
}
