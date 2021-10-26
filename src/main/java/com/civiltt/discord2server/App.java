package com.civiltt.discord2server;

import com.civiltt.discord2server.commands.SendMessage;
import com.civiltt.discord2server.commands.SendMessageTab;
import com.civiltt.discord2server.commands.SetColor;
import com.civiltt.discord2server.commands.SetColorTab;
import com.civiltt.discord2server.commands.SetDiscord;
import com.civiltt.discord2server.json.EditPlayers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {

    private FileConfiguration config;
    public static String no_token = "NoToken";

    @Override
    public void onEnable() {
        //config.ymlが存在しなかった場合は新しく作成をします。
        saveConfig();
        //config.ymlを読み込みます。
        config = getConfig();
        Discord2serverApplication.token = config.getString("Discord Bot Token", no_token);
        Discord2serverApplication.server_id = config.getLong("Discord Server ID");
        
        Discord2serverApplication api = new Discord2serverApplication();
        api.discordApi();

        SendMessage SendCommands = new SendMessage();
        SendMessageTab SendTab = new SendMessageTab();
        getCommand("re").setExecutor(SendCommands);
        getCommand("send").setExecutor(SendCommands);
        getCommand("send").setTabCompleter(SendTab);

        SetColor ColorCommands = new SetColor();
        SetColorTab ColorTab = new SetColorTab();
        getCommand("discordcolor").setExecutor(ColorCommands);
        getCommand("discordcolor").setTabCompleter(ColorTab);

        SetDiscord DiscordCommands = new SetDiscord();
        getCommand("mydiscordname").setExecutor(DiscordCommands);
        getCommand("mute").setExecutor(DiscordCommands);
        getCommand("unmute").setExecutor(DiscordCommands);

        getLogger().info("Hello, SpigotMC!");
    }

    @Override
    public void onDisable() {
        // PlayerDataをjsonに保存する
        EditPlayers.SetPlayers();

        // config.ymlを保存する
        config.options().header("You have to set Discord bot yourself\n\nThis is required item to use this plugin");
        config.set("Discord Bot Token", Discord2serverApplication.token);
        config.options().header("You can set your Discord Server's ID\n\nThis is not required item\nHowever if you don't set this, you can't use /mute and /unmute function");
        config.set("Discord Server ID", Discord2serverApplication.server_id);
        saveConfig();

        getLogger().info("See you again, SpigotMC!");
    }
}
