package dev.isnow.betterkingdoms.config.impl.commands;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class CommandNames implements Serializable {
    private String help;
    private String create, create_tab_completion;
    private String claim;
    private String disband, disband_admin_tab_completion;
    private String king;
    private String accept;
    private String invite;
    private String description, description_tab_completion;
    private String home;
    private String sethome;
    private String manualsave;


    public CommandNames() {} // Config system stuff
}
