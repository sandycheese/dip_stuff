package com.humanity.vs.cards.cardsvshumanity.logic.helpers;

import android.util.Log;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;

/**
 * Created by robot on 09.11.15.
 */
public class NetworkGameCommandsSender implements INetworkGameCommandsSender {

    NetworkManager networkManager;

    public NetworkGameCommandsSender(NetworkManager manager) {
        this.networkManager = manager;
    }

    @Override
    public void sendNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData) {
        Log.d(App.TAG, "Sending network game command: " + networkGameCommand);

        String dataClass = null;

        switch (networkGameCommand) {
            case gameStage1:
                dataClass = JsonGameStage1Data.class.toString();
                break;
            case gameStage2:
                dataClass = JsonGameStage2Data.class.toString();
                break;
            case gameStage3:
                dataClass = JsonGameStage3Data.class.toString();
                break;
            case gameStage4:
                dataClass = JsonGameStage4Data.class.toString();
                break;
        }

        networkManager.sendDataToDevice(dataClass, jsonData);
    }

    @Override
    public void endGame() {
        // todo implement
    }
}
