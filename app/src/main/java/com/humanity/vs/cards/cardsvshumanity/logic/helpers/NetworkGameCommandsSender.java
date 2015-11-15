package com.humanity.vs.cards.cardsvshumanity.logic.helpers;

import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommandDirection;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;

/**
 * Created by robot on 09.11.15.
 */
public class NetworkGameCommandsSender implements INetworkGameCommandsSender {
    @Override
    public void sendNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData, NetworkGameCommandDirection direction) {

    }

    @Override
    public String getClientNetworkId() {
        return null;
    }

    @Override
    public void endGame() {

    }
}
