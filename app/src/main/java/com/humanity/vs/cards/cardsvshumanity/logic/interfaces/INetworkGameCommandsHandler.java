package com.humanity.vs.cards.cardsvshumanity.logic.interfaces;

import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;

/**
 * Created by robot on 08.11.15.
 */
public interface INetworkGameCommandsHandler {
    void handleNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData);
}
