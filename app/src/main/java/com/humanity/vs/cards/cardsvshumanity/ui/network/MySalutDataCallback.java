package com.humanity.vs.cards.cardsvshumanity.ui.network;

import android.util.Log;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.peak.salut.Callbacks.SalutDataCallback;

/**
 * Created by robot on 17.11.15.
 */
public class MySalutDataCallback implements SalutDataCallback {
    @Override
    public void onDataReceived(Object o) {
        Log.d(App.TAG, "Some data recieved");
        Log.d(App.TAG, o.toString());

        Object q = o;
        Object w = q;
    }
}
