package com.humanity.vs.cards.cardsvshumanity.ui.network;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.humanity.vs.cards.cardsvshumanity.logic.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.logic.managers.GameManager;
import com.humanity.vs.cards.cardsvshumanity.ui.activities.MainActivity;
import com.humanity.vs.cards.cardsvshumanity.ui.entities_json.JsonPlayersInLobby;
import com.humanity.vs.cards.cardsvshumanity.ui.fragments.GameFragment;
import com.humanity.vs.cards.cardsvshumanity.ui.fragments.GameLobbyFragment;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.IGameManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.utils.ErrorsHelper;

/**
 * Created by robot on 19.11.15.
 */
public class AllNetworkDataHandler {
    private Activity activity;

    public AllNetworkDataHandler(Activity activity) {
        this.activity = activity;
    }

    public void onNetCmd_UpdatePlayersInLobbyList(String jsonStringData) {
        JsonPlayersInLobby jsonPlayersInLobby;
        try {
            jsonPlayersInLobby = new Gson().fromJson(jsonStringData, JsonPlayersInLobby.class);
        } catch (JsonSyntaxException e) {
            ErrorsHelper.commonError(activity);
            return;
        }

        if (activity instanceof MainActivity) {
            FragmentManager fragmentManager = activity.getFragmentManager();
            Fragment fragment = fragmentManager.findFragmentByTag(GameLobbyFragment.class.toString());
            if (fragment != null && fragment.isVisible()) {
                ((GameLobbyFragment) fragment).updateLobbyPlayersList(jsonPlayersInLobby.playersInLobby);
                return;
            }
        }

        ErrorsHelper.commonError(activity);
    }

    public void onNetCmd_HandleGameCommand(NetworkGameCommand gameCommand, String jsonStringData) {
        if (activity instanceof IGameManagerProvider) {
            GameManager gameManager = ((IGameManagerProvider) activity).getGameManager();
            if (gameManager != null) {
                gameManager.handleNetworkGameCommand(gameCommand, jsonStringData);
                return;
            }
        }

        ErrorsHelper.commonError(activity);
    }
}
