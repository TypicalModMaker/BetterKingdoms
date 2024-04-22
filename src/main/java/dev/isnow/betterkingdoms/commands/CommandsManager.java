package dev.isnow.betterkingdoms.commands;

import co.aikar.commands.PaperCommandManager;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.commands.impl.KingdomsCommand;

public class CommandsManager {

    @SuppressWarnings("ALL") // TODO: add command name translations and remove this suppression
    private final PaperCommandManager internalCommandManager;

    public CommandsManager(BetterKingdoms plugin) {
        internalCommandManager = new PaperCommandManager(plugin);

        internalCommandManager.enableUnstableAPI("brigadier");
        internalCommandManager.enableUnstableAPI("help");

        internalCommandManager.registerCommand(new KingdomsCommand());
    }
}
