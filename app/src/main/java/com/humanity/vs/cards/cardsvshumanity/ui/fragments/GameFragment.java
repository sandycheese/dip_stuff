package com.humanity.vs.cards.cardsvshumanity.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonPlayersWhiteDeck;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.IClientStageCallback;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.IGameUIUpdater;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.logic.managers.GameManager;
import com.humanity.vs.cards.cardsvshumanity.logic.repositories.CardsRepository;
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.WhiteCardsAdapter;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.IGameManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkGameCommandsSenderProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;
import com.humanity.vs.cards.cardsvshumanity.utils.ErrorsHelper;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by robot on 20.11.15.
 */
// todo make correct colors, margins, padding, font colors
// todo make errors texts
public class GameFragment extends Fragment implements IGameUIUpdater {

    GameManager gameManager = null;
    INetworkGameCommandsSender networkCommandsSender = null;
    NetworkManager networkManager = null;

    boolean startAsHost;

    MatchRules defaultMatchRules;
    int defaultScoreToWin = 5;

    View view;
    TextView tvBlackCard;
    RecyclerView rvWhiteCards;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);
        this.view = view;

        getActivity().setTitle(getActivity().getString(R.string.app_name));

        findComponents();
        initComponents();
        startGame();

        return view;
    }

    private void findComponents() {
        tvBlackCard = (TextView) view.findViewById(R.id.tvBlackCardText);
        rvWhiteCards = (RecyclerView) view.findViewById(R.id.rvWhiteCards);
    }

    private void initComponents() {
        this.defaultMatchRules = new MatchRules();
        this.defaultMatchRules.scoreToWin = this.defaultScoreToWin;

//        rvWhiteCards.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWhiteCards.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
    }

    private void startGame() {
        Activity activity = getActivity();
        if (activity instanceof IGameManagerProvider &&
                activity instanceof INetworkGameCommandsSenderProvider &&
                activity instanceof INetworkManagerProvider) {
            gameManager = ((IGameManagerProvider) activity).getGameManager();
            networkCommandsSender = ((INetworkGameCommandsSenderProvider) activity).getNetworkCommandsSender();
            networkManager = ((INetworkManagerProvider) activity).getNetworkManager();
        } else {
            ErrorsHelper.commonError(activity);
            // todo make operations
            return;
        }

        Bundle bundle = getArguments();

        if (bundle != null)
            startAsHost = bundle.getBoolean(GameLobbyFragment.ARG_START_AS_HOST);

        if (startAsHost) {

            ArrayList<GameClient> gameClients = new ArrayList<>();
            // todo at least 2 players check
            for (SalutDevice salutDevice : networkManager.getAllDevices()) {
                // todo is instanceName is ID?
                GameClient client = new GameClient(salutDevice.instanceName, salutDevice.deviceName);
                gameClients.add(client);
            }

            gameManager.newGameAsHost(
                    networkCommandsSender,
                    this,
                    gameClients,
                    CardsRepository.getAllCards(getActivity(), null),
                    defaultMatchRules);
        }
    }

    // todo remove hardcoded strings
    @Override
    public void makeStage1Updates(JsonGameStage1Data jsonGameStage1Data, IClientStageCallback stageCallback, String playerId) {
        if (jsonGameStage1Data.endGame) {
            getActivity().setTitle("End game");
            // todo operations
            return;
        }

        tvBlackCard.setText(jsonGameStage1Data.blackCard.text);
        getActivity().setTitle(String.format("Pick %s card%s",
                jsonGameStage1Data.blackCard.answersCount,
                jsonGameStage1Data.blackCard.answersCount > 1 ? "s" : ""));

        JsonPlayersWhiteDeck playerWhiteCards = null;
        for (JsonPlayersWhiteDeck deck : jsonGameStage1Data.playersWhiteDecks) {
            if (deck.playerId.equals(networkManager.thisDevice().instanceName)) {
                playerWhiteCards = deck;
                break;
            }
        }

        if (playerWhiteCards != null) {
            rvWhiteCards.setAdapter(new WhiteCardsAdapter(Arrays.asList(playerWhiteCards.whiteCards)));
        }

    }

    @Override
    public void makeStage3Updates(JsonGameStage3Data jsonGameStage3Data, IClientStageCallback stageCallback, String playerId) {

    }
}
