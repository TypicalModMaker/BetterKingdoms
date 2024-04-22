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
    private String create;
    private String claim;
    private String abandon;
    private String king;
    private String description;
    private String manualsave;

    public CommandNames() {} // Config system stuff
}
