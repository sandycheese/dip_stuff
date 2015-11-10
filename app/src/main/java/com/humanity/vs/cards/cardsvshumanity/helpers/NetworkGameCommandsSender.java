package com.humanity.vs.cards.cardsvshumanity.helpers;

import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommandDirection;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsSender;

/**
 * Created by robot on 09.11.15.
 */
public class NetworkGameCommandsSender implements INetworkGameCommandsSender {
    @Override
    public void sendNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData, NetworkGameCommandDirection direction) {

    }
}
