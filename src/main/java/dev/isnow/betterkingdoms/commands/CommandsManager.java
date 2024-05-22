package dev.isnow.betterkingdoms.commands;

import co.aikar.commands.Locales;
import co.aikar.commands.PaperCommandManager;
import dev.isnow.betterkingdoms.BetterKingdoms;
import dev.isnow.betterkingdoms.commands.impl.KingdomsCommand;
import dev.isnow.betterkingdoms.config.impl.commands.CommandConfig;
import dev.isnow.betterkingdoms.util.type.Locale;

public class CommandsManager {

    private final PaperCommandManager internalCommandManager;

    public CommandsManager(BetterKingdoms plugin) {
        internalCommandManager = new PaperCommandManager(plugin);
        internalCommandManager.getLocales().setDefaultLocale(plugin.getConfigManager().getCommandsConfig().getCommandsLocale().getJavaLocale());

//        internalCommandManager.enableUnstableAPI("brigadier");
//        internalCommandManager.enableUnstableAPI("help");

        init(plugin);

        internalCommandManager.registerCommand(new KingdomsCommand());
    }

    // TODO: FIX
    public void reload() {
        init(BetterKingdoms.getInstance());
    }

    public void unload() {
        internalCommandManager.unregisterCommands();
    }

    private void init(final BetterKingdoms plugin) {
        final CommandConfig commandConfig = plugin.getConfigManager().getCommandsConfig();
        internalCommandManager.getCommandReplacements().addReplacement("help", commandConfig.getHelp());

        internalCommandManager.getCommandReplacements().addReplacement("create", commandConfig.getCreate());
        internalCommandManager.getCommandReplacements().addReplacement("create_tab_completion", commandConfig.getCreate_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("claim", commandConfig.getClaim());
        internalCommandManager.getCommandReplacements().addReplacement("invite", commandConfig.getInvite());
        internalCommandManager.getCommandReplacements().addReplacement("accept", commandConfig.getAccept());

        internalCommandManager.getCommandReplacements().addReplacement("disband", commandConfig.getDisband());
        internalCommandManager.getCommandReplacements().addReplacement("admin_disband_tab_completion", commandConfig.getDisband_admin_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("king", commandConfig.getKing());

        internalCommandManager.getCommandReplacements().addReplacement("description", commandConfig.getDescription());
        internalCommandManager.getCommandReplacements().addReplacement("description_tab_completion", commandConfig.getDescription_tab_completion());

        internalCommandManager.getCommandReplacements().addReplacement("home", commandConfig.getHome());
        internalCommandManager.getCommandReplacements().addReplacement("sethome", commandConfig.getSethome());

        internalCommandManager.getCommandReplacements().addReplacement("manualsave", commandConfig.getManualsave());
    }
}
