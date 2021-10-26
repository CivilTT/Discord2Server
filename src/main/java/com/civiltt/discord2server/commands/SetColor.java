package com.civiltt.discord2server.commands;

import java.util.Optional;

import com.civiltt.discord2server.json.EditPlayers;
import com.civiltt.discord2server.json.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;


public class SetColor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String str, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            return true;
        }
        org.bukkit.entity.Player player = (org.bukkit.entity.Player) sender;

        // /discordcolor Discordから送られてきたメッセージの人の色を設定
        if(cmd.getName().equalsIgnoreCase("discordcolor")){
            if(args.length == 2){
                String GameName = player.getName();
                Player _Player = EditPlayers.PlayersData.getOrDefault(GameName, new Player());
                Optional<String> old_color_op = Optional.ofNullable(_Player.DCcolors.get(args[0]));
                String old_color = old_color_op.orElse("White");

                boolean can_continue = _Player.SetDCcolors_from_command(args[0], args[1]);
                if(!can_continue){
                    player.sendMessage(ChatColor.RED + "Invalid Arguments : The " + args[1] + " is not exists in Minecraft Default 16 Colors");
                }

                EditPlayers.AddPlayers(GameName, _Player);
                
                TextComponent text1 = new TextComponent("Changed the " + args[0] + "'s color from ");
                TextComponent text2 = new TextComponent(old_color);
                text2.setColor(SetColorTab.default16colorList.get(old_color));
                text2.setBold(true);
                TextComponent text3 = new TextComponent(" to ");
                TextComponent text4 = new TextComponent(args[1]);
                text4.setColor(SetColorTab.default16colorList.get(args[1]));
                text4.setBold(true);

                player.spigot().sendMessage(text1, text2, text3, text4);
            }else{
                player.sendMessage(ChatColor.RED + "Invalid Arguments : /discordcolor <DiscordName> <Color>");
            }

        }

        return true;
    }
}
