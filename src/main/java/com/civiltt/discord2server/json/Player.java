package com.civiltt.discord2server.json;

import java.util.HashMap;
import java.util.Map;

import com.civiltt.discord2server.commands.SetColorTab;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class Player {

    public String DCname;
    public long DCid;
    // Discordの名前をキーとして、設定されている色(原則、小文字で始まる)を取得
    public HashMap<String, String> DCcolors = new HashMap<String, String>();

    // Jsonからデータを登録する際に使用する
    public void SetDCcolors_from_json(JsonNode node){
        Map<String, String> map;
        ObjectMapper mapper = new ObjectMapper();

        String jsonStr = node.get("DCcolors").toString();
        try{
            map = mapper.readValue(jsonStr, new TypeReference<Map<String, String>>(){});
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String colorStr = entry.getValue();
                DCcolors.put(key, colorStr);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    // コマンドから呼ばれたときに色の情報を登録する
    // 続行不可能な場合はfalseを返す
    public boolean SetDCcolors_from_command(String name, String color){
        if(!SetColorTab.default16colorList.containsKey(color)){
            return false;
        }

        DCcolors.put(name, color);

        return true;
    }
}
