package dev.isnow.betterkingdoms.config.impl.commands;

import pl.mikigal.config.Config;
import pl.mikigal.config.annotation.ConfigName;

@ConfigName("commands.yml")
public interface CommandsConfig extends Config {
    default CommandNames getCommandNames() {
        return new CommandNames("pomoc",
                "zaloz", "<Nazwa_Królestwa>",
                "zajmij",
                "rozwiaz", "<Nazwa_Królestwa>",
                "krol",
                "akceptuj",
                "zapros",
                "opis", "<Opis_Królestwa>",
                "home",
                "sethome",
                "manualsave");
    }
}
