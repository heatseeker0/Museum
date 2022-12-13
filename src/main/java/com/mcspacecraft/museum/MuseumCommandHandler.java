package com.mcspacecraft.museum;

import org.bukkit.command.CommandSender;

import com.mcspacecraft.museum.util.ChatMessages;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CatchUnknown;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.CommandCompletion;
import co.aikar.commands.annotation.CommandPermission;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Subcommand;
import co.aikar.commands.annotation.Values;

// For more details on command annotations see https://aikar.github.io/commands/

@CommandAlias("museum|core|c")
@CommandPermission(MuseumConfig.ADMIN_PERMISSION_NODE)
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

    @Subcommand("chat")
    @CommandCompletion("@onoff")
    public void subCmdChat(CommandSender sender, @Values("@onoff") String value) {
        if (value.equals("on")) {
            plugin.getMuseumConfig().setChatDisabled(false);
            sender.sendMessage(ChatMessages.getMessage("admin.chat-enabled"));
        } else {
            plugin.getMuseumConfig().setChatDisabled(true);
            sender.sendMessage(ChatMessages.getMessage("admin.chat-disabled"));
        }
    }

    @Subcommand("login")
    @CommandCompletion("@onoff")
    public void subCmdLoginMessages(CommandSender sender, @Values("@onoff") String value) {
        if (value.equals("on")) {
            plugin.getMuseumConfig().showLoginMesages(true);
            sender.sendMessage(ChatMessages.getMessage("admin.login-msgs-enabled"));
        } else {
            plugin.getMuseumConfig().showLoginMesages(false);
            sender.sendMessage(ChatMessages.getMessage("admin.login-msgs-disabled"));
        }
    }
}
