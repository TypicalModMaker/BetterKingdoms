package dev.isnow.betterkingdoms.commands;

import co.aikar.commands.PaperCommandManager;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.commands.impl.KingdomsCommand;
import dev.isnow.betterkingdoms.config.impl.commands.CommandNames;

public class CommandsManager {

    @SuppressWarnings("ALL") // TODO: add command name translations and remove this suppression
    private final PaperCommandManager internalCommandManager;

    public CommandsManager(BetterKingdoms plugin) {
        internalCommandManager = new PaperCommandManager(plugin);

        internalCommandManager.enableUnstableAPI("brigadier");
        internalCommandManager.enableUnstableAPI("help");

        final CommandNames commandNames = plugin.getConfigManager().getCommandsConfig().getCommandNames();
        internalCommandManager.getCommandReplacements().addReplacement("help", commandNames.getHelp());
        internalCommandManager.getCommandReplacements().addReplacement("create", commandNames.getCreate());
        internalCommandManager.getCommandReplacements().addReplacement("claim", commandNames.getClaim());
        internalCommandManager.getCommandReplacements().addReplacement("abandon", commandNames.getAbandon());
        internalCommandManager.getCommandReplacements().addReplacement("king", commandNames.getKing());
        internalCommandManager.getCommandReplacements().addReplacement("description", commandNames.getDescription());
        internalCommandManager.getCommandReplacements().addReplacement("manualsave", commandNames.getManualsave());

        internalCommandManager.registerCommand(new KingdomsCommand());
    }
}
