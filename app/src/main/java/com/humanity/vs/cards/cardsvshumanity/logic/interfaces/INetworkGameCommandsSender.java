package com.humanity.vs.cards.cardsvshumanity.logic.interfaces;

import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommandDirection;

/**
 * Created by robot on 08.11.15.
 */
public interface INetworkGameCommandsSender {
    void sendNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData, NetworkGameCommandDirection direction);
    String getClientNetworkId();
    void endGame();
}
