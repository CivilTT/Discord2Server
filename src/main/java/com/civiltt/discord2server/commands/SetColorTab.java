package com.civiltt.discord2server.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import net.md_5.bungee.api.ChatColor;

public class SetColorTab implements TabCompleter {

    public static HashMap<String, ChatColor> default16colorList = new HashMap<String, ChatColor>(){
        {
            put("Aqua", ChatColor.AQUA);
            put("Black", ChatColor.BLACK);
            put("Blue", ChatColor.BLUE);
            put("Gold", ChatColor.GOLD);
            put("Gray", ChatColor.GRAY);
            put("DarkGray", ChatColor.DARK_GRAY);
            put("Green", ChatColor.GREEN);
            put("DarkGreen", ChatColor.DARK_GREEN);
            put("Purple", ChatColor.DARK_PURPLE);
            put("LightPurple", ChatColor.LIGHT_PURPLE);
            put("Red", ChatColor.RED);
            put("White", ChatColor.WHITE);
            put("Yellow", ChatColor.YELLOW);
        }
    };

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(command.getName().equalsIgnoreCase("discordcolor") && args.length == 2){
            Set<String> _result = default16colorList.keySet();
            List<String> result = new ArrayList<String>(_result);
            return result;
        }
        
        return null;
    }
    
}
