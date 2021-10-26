package com.civiltt.discord2server.commands;


import com.civiltt.discord2server.Discord2serverApplication;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.javacord.api.entity.channel.TextChannel;

import net.md_5.bungee.api.chat.TextComponent;

public class SendMessage implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player) sender;

        // /re 返信用のコマンド
        if (cmd.getName().equalsIgnoreCase("re")) {
            String name = player.getName();
            long id = Discord2serverApplication.LatestMessageChanelid;
            
            if(id == 0){
                player.sendMessage(ChatColor.RED + "Don't use this command before get a message from Discord");
                player.sendMessage(ChatColor.RED + "You have to use /send command to send a message");
                return true;
            }

            TextChannel text = Discord2serverApplication.api.getTextChannelById(id).orElse(null);
            text.sendMessage("<" + name + "> " + args[0]);
            TextComponent Message = new TextComponent(args[0]);
            // TextComponent Name = new TextComponent("<" + name + "> ");
            // Name.setColor(net.md_5.bungee.api.ChatColor.WHITE);
            // Bukkit.spigot().broadcast(Name, Message);
            Discord2serverApplication.SendEveryoneInGame(name, Message);
        }
        // /send チャット送信用のコマンド
        else if (cmd.getName().equalsIgnoreCase("send")) {
            if (args.length == 2) {
                String name = player.getName();
                if (!Discord2serverApplication.TextChannels.containsKey(args[0])) {
                    player.sendMessage(ChatColor.RED + "Channel " + args[0] + " is not exist in Discord Server");
                    return true;
                }
                
                String mes = "<" + name + "> " + args[1];
                long id = Discord2serverApplication.TextChannels.get(args[0]);
                TextChannel text = Discord2serverApplication.api.getTextChannelById(id).orElse(null);
                text.sendMessage(mes);
                // TextComponent sendMessage = new TextComponent(mes);
                // Bukkit.spigot().broadcast(sendMessage);
                TextComponent Message = new TextComponent(args[1]);
                Discord2serverApplication.SendEveryoneInGame(name, Message);

            } else {
                player.sendMessage(ChatColor.RED + "Invalid Arguments : /send <SendChannel> <Message>");
            }

        }

        return true;
    }

}
