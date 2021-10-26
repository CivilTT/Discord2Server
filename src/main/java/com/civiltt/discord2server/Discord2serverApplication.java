package com.civiltt.discord2server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.civiltt.discord2server.commands.SetColorTab;
import com.civiltt.discord2server.json.EditPlayers;
import com.civiltt.discord2server.json.Player;
// import com.civiltt.discord2server.listeners.MessageListener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.ServerVoiceChannel;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.*;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.bukkit.Bukkit;

@SpringBootApplication
public class Discord2serverApplication {

	public static String token;
	public static long server_id;

	public static long LatestMessageChanelid = 0;
	public static DiscordApi api;

	public static HashMap<String, Long> TextChannels = new HashMap<String, Long>();
	public static HashMap<String, Long> VoiceChannels = new HashMap<String, Long>();

	// @Autowired
	// private MessageListener messageListener;

	public static void main(String[] args) {
		SpringApplication.run(Discord2serverApplication.class, args);
	}

	@Bean
	@ConfigurationProperties(value = "discord-api")
	public DiscordApi discordApi() {
		api = new DiscordApiBuilder().setToken(token).setAllNonPrivilegedIntents().login().join();
		api.updateActivity("Minecraft Spigot Server");

		// Discord のチャンネル一覧を取得
		GetEnv();

		// Helloと打つと「キモッ」と返す
		api.addMessageCreateListener(event -> {
			if (event.getMessageContent().equals("Hello")) {
				event.getChannel().sendMessage("キモッ");
			}
		});

		// 通話の入退出を検知
		api.addServerVoiceChannelMemberLeaveListener(event -> {
			String name = event.getUser().getName();
			String channel = event.getChannel().getName();
			// TextChannel text = api.getTextChannelById(869243757915156510L).orElse(null);
			// text.sendMessage(name+"が"+channel+"から退出しました");

			// getLogger().info(name+"が"+channel+"から退出しました");
			TextComponent str = new TextComponent(name + "が" + channel + "から退出しました");
			str.setColor(ChatColor.YELLOW);
			Bukkit.spigot().broadcast(str);
		});
		api.addServerVoiceChannelMemberJoinListener(event -> {
			String name = event.getUser().getName();
			String channel = event.getChannel().getName();
			// TextChannel text = api.getTextChannelById(869243757915156510L).orElse(null);
			// text.sendMessage(name+"が"+channel+"に入室しました");

			// getLogger().info(name+"が"+channel+"に入室しました");
			TextComponent str = new TextComponent(name + "が" + channel + "に入室しました");
			str.setColor(ChatColor.YELLOW);
			Bukkit.spigot().broadcast(str);
		});

		// メッセージの送受信を検知
		api.addMessageCreateListener(event -> {
			// コマンドではないメッセージに対して処理を行う
			if(!event.getMessageContent().startsWith("!") || !event.getMessageContent().startsWith("/")){
				// botが送ったメッセージには反応しないようにする
				if(!event.getMessageAuthor().getName().equals(Discord2serverApplication.api.getYourself().getName())){
					// /re コマンドが使えるように最新の会話をしたチャンネルを保存
					Discord2serverApplication.LatestMessageChanelid = event.getChannel().getId();

					String name = event.getMessageAuthor().getName();
					
					CheckAttachment(event, name);
					CheckURL(event, name);
				}
			}
		});

		return api;
	}

	

    // 送られてきたメッセージからURLとその他を分離＆改行についても分離
	private void CheckURL(MessageCreateEvent event, String name) {
		Pattern urlPattern = Pattern.compile("https?://[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);
		Matcher urls = urlPattern.matcher(event.getMessageContent());
		String[] messages = urlPattern.split(event.getMessageContent());
		
		// 通常のメッセージを処理
		for (String message : messages) {
			// 改行コードを抜かす
			for (String part_message : message.split("\n")){
				if(part_message == "") continue;
				TextComponent component_message = new TextComponent(part_message);
				// Bukkit.spigot().broadcast(name, component_message);
                SendEveryoneInGame(name, component_message);
			}
		}

		// URLを処理
		while(urls.find()){
			TextComponent url = new TextComponent("OPEN URL");
			url.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urls.group()));
			url.setUnderlined(true);
			// Bukkit.spigot().broadcast(name, url);
            SendEveryoneInGame(name, url);
		}
	}

	// 写真などの添付ファイルを分離
	private void CheckAttachment(MessageCreateEvent event, String name) {
		TextComponent text = new TextComponent();
		text.setUnderlined(true);

		for (MessageAttachment attachment : event.getMessageAttachments()) {
			text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, attachment.getUrl().toString()));
			if(attachment.isImage()){
				text.setText("Picture");
			}else{
				text.setText("Attachment");
			}

			// Bukkit.spigot().broadcast(name, text);
            SendEveryoneInGame(name, text);
		}
	}

    // 各個人の設定に沿って色づいた文字を表示
    public static void SendEveryoneInGame(String name_Sender, TextComponent text){
        TextComponent text_component = new TextComponent(text);

        for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
            String name_InGame = player.getName();
			// Player.jsonにデータが存在しない場合は規定値の白色で出力
            Player player_data = EditPlayers.PlayersData.getOrDefault(name_InGame, new Player());
			String colorStr = player_data.DCcolors.getOrDefault(name_Sender, "White");
			colorStr = colorStr.substring(0,1).toUpperCase() + colorStr.substring(1);
			ChatColor color = SetColorTab.default16colorList.get(colorStr);

			String name_text = "<" + name_Sender + "> ";
            TextComponent name_component = new TextComponent(name_text);
            name_component.setColor(color);
			name_component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/discordcolor " + name_Sender + " "));
			name_component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
				new ComponentBuilder("Click here to change the player color").color(ChatColor.GRAY).italic(true).create()));

            player.spigot().sendMessage(name_component, text_component);
			System.out.println(name_text + text.getText());
        }
    }

	private void GetEnv() {
		GetTextChannels();
		GetVoiceChannels();
		// GetUsers();
		
		try {
			GetServerid();
			EditPlayers.GetPlayers();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void GetServerid() throws IOException {
		Iterator<Server> servers = api.getServers().iterator();
		if(api.getServers().size() == 1){
			server_id = servers.next().getId();
		}
		else{
			throw new IOException("This bot is able to be imported only one Discord Server");
		}
	}

	private void GetTextChannels() {
		Iterator<ServerTextChannel> itrTextChannels = api.getServerTextChannels().iterator();
		while (itrTextChannels.hasNext()) {
			ServerTextChannel channel = itrTextChannels.next();
			String name = channel.getName();
			Long id = channel.getId();
			TextChannels.put(name, id);
		}
	}

	private void GetVoiceChannels() {
		Iterator<ServerVoiceChannel> itrVoiceChannels = api.getServerVoiceChannels().iterator();
		while (itrVoiceChannels.hasNext()) {
			ServerVoiceChannel channel = itrVoiceChannels.next();
			String name = channel.getName();
			Long id = channel.getId();
			VoiceChannels.put(name, id);
		}
	}

	// // サーバーに参加している全参加者の名前を取得（未完）
	// private void GetUsers(){
	// Iterator<Server> server = api.getServers().iterator();
	// while(server.hasNext()){
	// Server user = server.next();
	// api.getYourself().mute(user);
	// // System.out.println(user.getName());
	// // Iterator<User> name = user.getMembers().iterator();
	// // System.out.println(user.getMembers().size());
	// // while(name.hasNext()){
	// // System.out.println(name.next().getName());
	// // }
	// }
	// }
}
