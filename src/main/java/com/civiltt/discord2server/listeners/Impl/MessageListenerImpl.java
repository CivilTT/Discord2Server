// package com.civiltt.discord2server.listeners.Impl;

// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// import com.civiltt.discord2server.Discord2serverApplication;
// import com.civiltt.discord2server.json.EditPlayers;
// import com.civiltt.discord2server.json.Player;
// import com.civiltt.discord2server.listeners.MessageListener;

// import org.bukkit.Bukkit;
// import org.javacord.api.entity.message.MessageAttachment;
// import org.javacord.api.event.message.MessageCreateEvent;
// import org.springframework.stereotype.Component;

// import net.md_5.bungee.api.ChatColor;
// import net.md_5.bungee.api.chat.ClickEvent;
// import net.md_5.bungee.api.chat.TextComponent;

// @Component
// public class MessageListenerImpl implements MessageListener {

//     @Override
//     public void onMessageCreate(MessageCreateEvent event) {
//         // コマンドではないメッセージに対して処理を行う
//         if(!event.getMessageContent().startsWith("!") || !event.getMessageContent().startsWith("/")){
//             // botが送ったメッセージには反応しないようにする
//             if(!event.getMessageAuthor().getName().equals(Discord2serverApplication.api.getYourself().getName())){
//                 // /re コマンドが使えるように最新の会話をしたチャンネルを保存
//                 Discord2serverApplication.LatestMessageChanelid = event.getChannel().getId();

//                 // String _message = event.getMessageContent() + " ";
//                 // TextComponent message = new TextComponent(_message);
                
//                 // String _name = "<" + event.getMessageAuthor().getName() + "> ";
//                 String name = event.getMessageAuthor().getName();
// 				System.out.println(name);
                
//                 CheckAttachment(event, name);
//                 CheckURL(event, name);
                
//                 // Bukkit.spigot().broadcast(name, message, attachment);
//             }
//         }
        
//     }

//     // 送られてきたメッセージからURLとその他を分離＆改行についても分離
// 	private void CheckURL(MessageCreateEvent event, String name) {
// 		Pattern urlPattern = Pattern.compile("https?://[\\w\\.\\-/:\\#\\?\\=\\&\\;\\%\\~\\+]+", Pattern.CASE_INSENSITIVE);
// 		Matcher urls = urlPattern.matcher(event.getMessageContent());
// 		String[] messages = urlPattern.split(event.getMessageContent());
		
// 		// 通常のメッセージを処理
// 		for (String message : messages) {
// 			System.out.println(message);
// 			// 改行コードを抜かす
// 			for (String part_message : message.split("\n")){
// 				if(part_message == "") continue;
// 				TextComponent component_message = new TextComponent(part_message);
// 				// Bukkit.spigot().broadcast(name, component_message);
//                 SendEveryoneInGame(name, component_message);
// 			}
// 		}

// 		// URLを処理
// 		while(urls.find()){
// 			TextComponent url = new TextComponent("OPEN URL");
// 			url.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, urls.group()));
// 			url.setUnderlined(true);
// 			// Bukkit.spigot().broadcast(name, url);
//             SendEveryoneInGame(name, url);
// 		}
// 	}

// 	// 写真などの添付ファイルを分離
// 	private void CheckAttachment(MessageCreateEvent event, String name) {
// 		TextComponent text = new TextComponent();
// 		text.setUnderlined(true);

// 		for (MessageAttachment attachment : event.getMessageAttachments()) {
// 			text.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, attachment.getUrl().toString()));
// 			if(attachment.isImage()){
// 				text.setText("Picture");
// 			}else{
// 				text.setText("Attachment");
// 			}

// 			// Bukkit.spigot().broadcast(name, text);
//             SendEveryoneInGame(name, text);
// 		}
// 	}

//     // 各個人の設定に沿って色づいた文字を表示
//     private void SendEveryoneInGame(String name_Sender, TextComponent text){
//         TextComponent text_component = new TextComponent(text);

//         for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
//             String name_InGame = player.getName();
// 			// Player.jsonにデータが存在しない場合は規定値の白色で出力
//             Player player_data = EditPlayers.PlayersData.getOrDefault(name_InGame, new Player());
//             ChatColor color = player_data.DCcolors.getOrDefault(name_Sender, ChatColor.WHITE);

//             TextComponent name_component = new TextComponent("<" + name_Sender + "> ");
//             name_component.setColor(color);

//             player.spigot().sendMessage(name_component, text_component);
//         }
//     }
    
// }
