package dev.isnow.betterkingdoms.commands;

import co.aikar.commands.PaperCommandManager;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.commands.impl.KingdomsCommand;
import dev.isnow.betterkingdoms.config.impl.commands.CommandNames;

public class CommandsManager {

    private final PaperCommandManager internalCommandManager;

    public CommandsManager(BetterKingdoms plugin) {
        internalCommandManager = new PaperCommandManager(plugin);

//        internalCommandManager.enableUnstableAPI("brigadier");
//        internalCommandManager.enableUnstableAPI("help");

        final CommandNames commandNames = plugin.getConfigManager().getCommandsConfig().getCommandNames();

        init(commandNames);

        internalCommandManager.registerCommand(new KingdomsCommand());
    }

    // TODO: FIX
    public void reload() {
        final CommandNames commandNames = BetterKingdoms.getInstance().getConfigManager().getCommandsConfig().getCommandNames();

        init(commandNames);
    }

    public void unload() {
        internalCommandManager.unregisterCommands();
    }

    private void init(final CommandNames commandNames) {
        internalCommandManager.getCommandReplacements().addReplacement("help", commandNames.getHelp());

        internalCommandManager.getCommandReplacements().addReplacement("create", commandNames.getCreate());
        internalCommandManager.getCommandReplacements().addReplacement("create_tab_completion", commandNames.getCreate_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("claim", commandNames.getClaim());
        internalCommandManager.getCommandReplacements().addReplacement("invite", commandNames.getInvite());
        internalCommandManager.getCommandReplacements().addReplacement("accept", commandNames.getAccept());

        internalCommandManager.getCommandReplacements().addReplacement("disband", commandNames.getDisband());
        internalCommandManager.getCommandReplacements().addReplacement("admin_disband_tab_completion", commandNames.getDisband_admin_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("king", commandNames.getKing());

        internalCommandManager.getCommandReplacements().addReplacement("description", commandNames.getDescription());
        internalCommandManager.getCommandReplacements().addReplacement("description_tab_completion", commandNames.getDescription_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("home", commandNames.getHome());
        internalCommandManager.getCommandReplacements().addReplacement("sethome", commandNames.getSethome());

        internalCommandManager.getCommandReplacements().addReplacement("manualsave", commandNames.getManualsave());
    }
}
