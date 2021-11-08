package com.civiltt.discord2server.commands;

import com.civiltt.discord2server.App;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class PluginFunc implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reboot")) {
            App.config = YamlConfiguration.loadConfiguration(App.cfile);
            App.launchBot();
        }

        return true;
    }

}
