package com.humanity.vs.cards.cardsvshumanity.ui.network;

import android.util.Log;

import com.bluelinelabs.logansquare.LoganSquare;
import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonGodLevelData;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonPlayersInLobby;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;
import com.peak.salut.Callbacks.SalutDataCallback;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by robot on 17.11.15.
 */
public class MySalutDataCallback implements SalutDataCallback {

    private AllNetworkDataHandler allHandler;

    public MySalutDataCallback(AllNetworkDataHandler allHandler) {

        this.allHandler = allHandler;
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

        // lobby players update
        if (isClassOf(JsonPlayersInLobby.class, data.classNameOfData)) {
            allHandler.onNetCmd_UpdatePlayersInLobbyList(data.jsonStringData);
        }
        // game commands
        else if (isClassOf(JsonGameStage1Data.class, data.classNameOfData) ||
                isClassOf(JsonGameStage2Data.class, data.classNameOfData) ||
                isClassOf(JsonGameStage3Data.class, data.classNameOfData) ||
                isClassOf(JsonGameStage4Data.class, data.classNameOfData)) {

            NetworkGameCommand gameCommand = null;
            if (isClassOf(JsonGameStage1Data.class, data.classNameOfData))
                gameCommand = NetworkGameCommand.gameStage1;
            if (isClassOf(JsonGameStage2Data.class, data.classNameOfData))
                gameCommand = NetworkGameCommand.gameStage2;
            if (isClassOf(JsonGameStage3Data.class, data.classNameOfData))
                gameCommand = NetworkGameCommand.gameStage3;
            if (isClassOf(JsonGameStage4Data.class, data.classNameOfData))
                gameCommand = NetworkGameCommand.gameStage4;

            allHandler.onNetCmd_HandleGameCommand(gameCommand, data.jsonStringData);
        }
    }

    private boolean isClassOf(Type className, String classNameString) {
        return className.toString().equals(classNameString);
    }
}
