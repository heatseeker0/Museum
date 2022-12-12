package com.mcspacecraft.museum;

import org.bukkit.command.CommandSender;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;

// For more details on command annotations see https://aikar.github.io/commands/

@CommandAlias("museum|core|c")
@CommandPermission("museum.admin")
public class MuseumCommandHandler extends BaseCommand {
    private Museum plugin;

    public MuseumCommandHandler(Museum plugin) {
        this.plugin = plugin;
    }

    @Subcommand("help")
    @CatchUnknown
    @Default
    public void doHelp(CommandSender sender) {
        sender.sendMessage(ChatMessages.getMessage("cmd-help"));
    }

    @Subcommand("reload")
    public void subCmdReload(CommandSender sender) {
        plugin.getMuseumConfig().load();
        sender.sendMessage(ChatMessages.getMessage("configuration-loaded"));
    }
}
