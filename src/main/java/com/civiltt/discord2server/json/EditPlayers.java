package com.civiltt.discord2server.json;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import com.civiltt.discord2server.Discord2serverApplication;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EditPlayers {

	private static String path = "plugins/" + Discord2serverApplication.api.getYourself().getName() + "/Players.json";

	// キーとしてマイクラ内でのプレイヤー名を使い、Discordに関する情報をPlayerに保存
    public static Map<String, Player> PlayersData = new HashMap<String, Player>();

	public static void AddPlayers(String name, Player player){
		PlayersData.put(name, player);
	}

    public static void GetPlayers() throws IOException {
		File file = new File(path);

		// ファイルが存在しない場合は作成だけする
		if(!file.exists()){
			file.createNewFile();
			return;
		}

		// データを読み取り、Optionalとして保管する
		// Optionalにすることで、nullの時の規定値を割り当てやすくする
		Path _file = Paths.get(path);
		String text = Files.readString(_file);

		Map<String, Player> map;
        ObjectMapper mapper = new ObjectMapper();

		map = mapper.readValue(text, new TypeReference<Map<String, Player>>(){});
		for (Map.Entry<String, Player> entry : map.entrySet()){
			String MCname = entry.getKey();
			Player player = entry.getValue();
			AddPlayers(MCname, player);
		}
		// ObjectMapper mapper = new ObjectMapper();
		// JsonNode node = mapper.readTree(file);

		// for (JsonNode jsonNode : node) {
        //     Player player = new Player();
        //     String mc_name = jsonNode..get("MCname").toString();

        //     player.DCname = jsonNode.get("DCname").toString();
        //     player.SetDCcolors_from_json(jsonNode);

		// 	AddPlayers(mc_name, player);
		// }
	}

	public static void SetPlayers(){
		// JSONObject jsonObject = new JSONObject(PlayersData);
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);

		try (FileWriter file = new FileWriter(path)){
			String jsonStr = mapper.writeValueAsString(PlayersData);
			file.write(jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
