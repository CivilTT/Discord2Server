package com.civiltt.discord2server.commands;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

import com.civiltt.discord2server.Discord2serverApplication;
import com.civiltt.discord2server.json.EditPlayers;
import com.civiltt.discord2server.json.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.javacord.api.DiscordApi;
import org.javacord.api.entity.user.User;

import net.md_5.bungee.api.ChatColor;

public class SetDiscord implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;
        Player player_data = EditPlayers.PlayersData.get(player.getName());

        // /mydiscordname Discord上での自身の名称を設定する
        if(cmd.getName().equalsIgnoreCase("mydiscordname")){
            if(args.length == 1){
                Collection<User> users = Discord2serverApplication.api.getCachedUsersByName(args[0]);
                if(users.size() == 0){
                    player.sendMessage(args[0] + " doesn't participant in the Discord Server");
                    return true;
                }
                
                player_data.DCname = args[0];
                for (User user : users) {
                    player_data.DCid = user.getId();
                }

            }else{
                player.sendMessage(ChatColor.RED + "Inavalid Arguments : /mydiscordname <your name in discord>");
            }
        }

        if(cmd.getName().equalsIgnoreCase("mute")){
            if(args.length == 0){
                Long dc_id = player_data.DCid;
                if(dc_id == 0){
                    player.sendMessage(ChatColor.RED + "Invalid Settings : You didn't set your Discord name. Use /mydiscordname command and set it");
                }

                DiscordApi api = Discord2serverApplication.api;
                try {
                    api.getUserById(dc_id).get().mute(api.getServerById(Discord2serverApplication.server_id).orElse(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return true;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return true;
                }

                player.sendMessage(ChatColor.GREEN + "Success to mute your voice");
            }
        }

        if(cmd.getName().equalsIgnoreCase("unmute")){
            if(args.length == 0){
                Long dc_id = player_data.DCid;
                if(dc_id == 0){
                    player.sendMessage(ChatColor.RED + "Invalid Settings : You didn't set your Discord name. Use /mydiscordname command and set it");
                }

                DiscordApi api = Discord2serverApplication.api;
                try {
                    api.getUserById(dc_id).get().unmute(api.getServerById(Discord2serverApplication.server_id).orElse(null));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return true;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    return true;
                }

                player.sendMessage(ChatColor.GREEN + "Success to unmute your voice");
            }
        }

        return true;
    }
    
}
