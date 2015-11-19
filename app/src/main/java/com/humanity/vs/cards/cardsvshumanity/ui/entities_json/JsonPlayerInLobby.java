package com.humanity.vs.cards.cardsvshumanity.ui.entities_json;

import com.peak.salut.SalutDevice;

/**
 * Created by robot on 19.11.15.
 */
public class JsonPlayerInLobby {
    public String deviceName;
    public String readableName;

    public JsonPlayerInLobby() {
    }

    public JsonPlayerInLobby(SalutDevice salutDevice) {
        this.deviceName = salutDevice.deviceName;
        this.readableName = salutDevice.readableName;
    }
}
