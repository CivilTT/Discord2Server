package com.civiltt.discord2server;

import java.io.File;
import java.io.IOException;

import com.civiltt.discord2server.commands.PluginFunc;
import com.civiltt.discord2server.commands.SendMessage;
import com.civiltt.discord2server.commands.SendMessageTab;
import com.civiltt.discord2server.commands.SetColor;
import com.civiltt.discord2server.commands.SetColorTab;
import com.civiltt.discord2server.commands.SetDiscord;
import com.civiltt.discord2server.json.EditPlayers;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class App extends JavaPlugin {

    public static FileConfiguration config;
    public static File cfile;

    @Override
    public void onEnable() {
        readConfig();
        
        launchBot();

        registCommands();

        // getLogger().info("Hello, SpigotMC!");
    }

    @Override
    public void onDisable() {
        // PlayerDataをjsonに保存する
        EditPlayers.SetPlayers();

        // config.ymlを保存する
        config.options().header("You have to set Discord bot yourself");
        config.options().header("This is required item to use this plugin");
        config.set("Discord Bot Token", Discord2serverApplication.token);
        config.options().header("");
        config.options().header("You can set your Discord Server's ID");
        config.options().header("This is not required item");
        config.options().header("However if you don't set this, you can't use /mute and /unmute function");
        config.set("Discord Server ID", Discord2serverApplication.server_id);

        try {
            config.save(cfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // getLogger().info("See you again, SpigotMC!");
    }

    private void readConfig(){
        //config.ymlを読み込みます。
        config = getConfig();
        // デフォルトのconfigをコピーします。
        config.options().copyDefaults(true);
        //config.ymlが存在しなかった場合は新しく作成をします。
        saveConfig();
        cfile = new File(getDataFolder(), "config.yml");
    }

    public static void launchBot(){
        try {
            Discord2serverApplication.token = config.getString("Discord Bot Token");
            Discord2serverApplication.server_id = config.getLong("Discord Server ID");

            Discord2serverApplication api = new Discord2serverApplication();
            api.discordApi();
            System.out.println("Success to launch Discord Bot at " + Discord2serverApplication.server_name);
        } catch (Exception e) {
            System.out.println("Failed to launch Discord Bot");
            System.out.println("You have to edit 'Discord Bot Token' in config.yml");
            System.out.println("Then, Type `reboot` in this console");
        }
    }

    private void registCommands(){
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

        PluginFunc pluginFunc = new PluginFunc();
        getCommand("reboot").setExecutor(pluginFunc);
    }
}
