package com.humanity.vs.cards.cardsvshumanity.ui.network;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonGodLevelData;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonPlayersInLobby;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;
import com.peak.salut.Callbacks.SalutDataCallback;

import java.io.IOException;

/**
 * Created by robot on 17.11.15.
 */
public class MySalutDataCallback implements SalutDataCallback {

    private AllNetworkDataHandler handler;

    public MySalutDataCallback(AllNetworkDataHandler handler) {

        this.handler = handler;
    }

    @Override
    public void onDataReceived(Object o) {
        Log.d(App.TAG, "Some data received");
        Log.d(App.TAG, o.toString());

        JsonGodLevelData data;
        try {
            data = LoganSquare.parse(o.toString(), JsonGodLevelData.class);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(App.TAG, "Received data cannot be parsed");
            return;
        }

        if (data == null || EmptyUtils.isEmpty(data.classNameOfData) || EmptyUtils.isEmpty(data.jsonStringData)) {
            Log.d(App.TAG, "Received data is empty");
            return;
        }

        if (JsonPlayersInLobby.class.toString().equals(data.classNameOfData)) {
            handler.updatePlayersInLobbyList(data.jsonStringData);
        }

    }
}
