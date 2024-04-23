package dev.isnow.betterkingdoms.config.impl.commands;

import de.exlll.configlib.Configuration;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Configuration
public class CommandConfig {

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
    String manualsave = "manualsave";
}
