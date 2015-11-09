package com.humanity.vs.cards.cardsvshumanity.interfaces;

import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;

/**
 * Created by robot on 08.11.15.
 */
public interface INetworkGameCommandsHandler {
    void handleNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData);
}
