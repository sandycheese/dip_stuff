package com.humanity.vs.cards.cardsvshumanity.ui.interfaces;

import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;

/**
 * Created by robot on 21.11.15.
 */
public interface INetworkGameCommandsSenderProvider {
    INetworkGameCommandsSender getNetworkCommandsSender();
}
