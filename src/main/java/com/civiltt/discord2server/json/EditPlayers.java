package com.civiltt.discord2server.json;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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

		try {
			File file = new File(path);
			PrintWriter p_writer = new PrintWriter(new BufferedWriter
                (new OutputStreamWriter(new FileOutputStream(file),"Shift-JIS")));
			String jsonStr = mapper.writeValueAsString(PlayersData);
			
			p_writer.write(jsonStr);

			p_writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
