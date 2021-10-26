package com.civiltt.discord2server.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.civiltt.discord2server.Discord2serverApplication;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SendMessageTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("send") && args.length == 1){
            Set<String> _result = Discord2serverApplication.TextChannels.keySet();
            List<String> result = new ArrayList<String>(_result);
            return result;
        }

        return new ArrayList<String>();
    }
    
}
