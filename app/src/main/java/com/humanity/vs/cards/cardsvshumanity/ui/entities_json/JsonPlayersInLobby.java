package com.humanity.vs.cards.cardsvshumanity.ui.entities_json;

import com.google.gson.Gson;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;

/**
 * Created by robot on 19.11.15.
 */
public class JsonPlayersInLobby {
    public JsonPlayerInLobby[] playersInLobby;

    public static JsonGodLevelData getJsonGodLevelData(ArrayList<SalutDevice> devices) {
        JsonGodLevelData data = new JsonGodLevelData();

        JsonPlayersInLobby jsonPlayersInLobby = new JsonPlayersInLobby();

        jsonPlayersInLobby.playersInLobby = new JsonPlayerInLobby[devices.size()];
        for (int i = 0; i < devices.size(); i++) {
            SalutDevice device = devices.get(i);

            JsonPlayerInLobby jsonPlayerInLobby = new JsonPlayerInLobby(device);

            jsonPlayersInLobby.playersInLobby[i] = jsonPlayerInLobby;
        }

        String jsonString;

        jsonString = new Gson().toJson(jsonPlayersInLobby, JsonPlayersInLobby.class);

        data.classNameOfData = JsonPlayersInLobby.class.toString();
        data.jsonStringData = jsonString;

        return data;
    }
}
