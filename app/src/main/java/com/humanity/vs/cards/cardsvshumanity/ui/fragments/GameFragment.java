package com.humanity.vs.cards.cardsvshumanity.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonPlayerState;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonPlayersWhiteDeck;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonWhiteCardsSelection;
import com.humanity.vs.cards.cardsvshumanity.logic.helpers.GameManagerHelper;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.IClientStageCallback;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.IGameUIUpdater;
import com.humanity.vs.cards.cardsvshumanity.logic.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.logic.managers.GameManager;
import com.humanity.vs.cards.cardsvshumanity.logic.repositories.CardsRepository;
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.SelectionAdapter;
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
// todo make correct colors, margins, padding, font colors, elevations
// todo make errors texts
public class GameFragment extends Fragment implements IGameUIUpdater {

    public static final String CLIENT_NEW_GAME_STAGE_1_DATA = "CLIENT_NEW_GAME_STAGE_1_DATA";

    GameManager gameManager = null;
    INetworkGameCommandsSender networkCommandsSender = null;
    NetworkManager networkManager = null;

    boolean startAsHost;

    MatchRules defaultMatchRules;
    int defaultScoreToWin = 5;

    View view;
    TextView tvBlackCard;
    RecyclerView rvWhiteCards;
    LinearLayout llMessageToPlayer;
    TextView tvMessageToPlayer;
    ImageView ivMessageToPlayer;
    Snackbar sbSendSelectedCards = null;

    boolean isKingNow = false;

    IClientStageCallback stageCallback;

    WhiteCardsAdapter.WhiteCardsAdapter_CardsSelectedCallback selectedCardsCallback = new WhiteCardsAdapter.WhiteCardsAdapter_CardsSelectedCallback() {
        @Override
        public void selected(final ArrayList<JsonCard> selectedCards) {
            // todo is it only for selection? if so don't init every time
            initSnackbarForSelectedCardsSend(selectedCards);
            if (sbSendSelectedCards != null && !sbSendSelectedCards.isShown()) {
                sbSendSelectedCards.show();
            }
        }

        @Override
        public void notSelectedYet() {
            if (sbSendSelectedCards != null)
                sbSendSelectedCards.dismiss();
        }
    };

    SelectionAdapter.SelectionAdapter_SelectCallback selectedSelectionCallback = new SelectionAdapter.SelectionAdapter_SelectCallback() {
        @Override
        public void selected(JsonWhiteCardsSelection selection) {
            initSnackbarForSelectedSelectionSend(selection);
            if (sbSendSelectedCards != null && !sbSendSelectedCards.isShown()) {
                sbSendSelectedCards.show();
            }
        }

        @Override
        public void notSelectedYet() {
            if (sbSendSelectedCards != null)
                sbSendSelectedCards.dismiss();
        }
    };

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

    void initSnackbarForSelectedCardsSend(final ArrayList<JsonCard> selectedCards) {
        try {
            sbSendSelectedCards = Snackbar.make(view, R.string.text_question_done, Snackbar.LENGTH_INDEFINITE);
            sbSendSelectedCards.setAction(R.string.text_yes, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendStage2Data(selectedCards);
                }
            });
        } catch (Exception e) {
            Log.d(App.TAG, "Unexpected application behaviour");
        }
    }

    void initSnackbarForSelectedSelectionSend(final JsonWhiteCardsSelection selectedSelection) {
        try {
            sbSendSelectedCards = Snackbar.make(view, R.string.text_question_done, Snackbar.LENGTH_INDEFINITE);
            sbSendSelectedCards.setAction(R.string.text_yes, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendStage4Data(selectedSelection);
                }
            });
        } catch (Exception e) {
            Log.d(App.TAG, "Unexpected application behaviour");
        }
    }


    private void findComponents() {
        tvBlackCard = (TextView) view.findViewById(R.id.tvBlackCardText);
        rvWhiteCards = (RecyclerView) view.findViewById(R.id.rvWhiteCards);
        llMessageToPlayer = (LinearLayout) view.findViewById(R.id.llMessageToPlayer);
        tvMessageToPlayer = (TextView) view.findViewById(R.id.tvMessageToPlayer);
        ivMessageToPlayer = (ImageView) view.findViewById(R.id.ivMessageToPlayer);
    }

    private void initComponents() {
        this.defaultMatchRules = new MatchRules();
        this.defaultMatchRules.scoreToWin = this.defaultScoreToWin;

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
            startAsHost = bundle.getBoolean(GameLobbyFragment.ARG_START_SERVICE);

        // HOST new game
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
        // CLIENT new game
        else {
            String jsonStage1DataString;
            if (bundle != null) {
                jsonStage1DataString = bundle.getString(GameFragment.CLIENT_NEW_GAME_STAGE_1_DATA);
            } else {
                Log.d(App.TAG, "Invalid app behaviour");
                return;
            }

            JsonGameStage1Data stage1Data = GameManagerHelper.getStage1DataFromJson(jsonStage1DataString);
            makeStage1Updates(stage1Data, null);

            gameManager.newGameAsClient(networkCommandsSender, this);
        }
    }

    @Override
    public void makeStage1Updates(JsonGameStage1Data jsonGameStage1Data, IClientStageCallback stageCallback) {
        this.stageCallback = stageCallback;

        rvWhiteCards.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // check end game
        if (jsonGameStage1Data.endGame) {
            Log.d(App.TAG, "End game detected");
            getActivity().setTitle(getActivity().getString(R.string.text_end_game));
            // todo operations
            return;
        }

        // check round result
        // todo implement

        // check is king
        isKingNow = isCurrentPlayerKing(jsonGameStage1Data.playerStates);

        if (isKingNow) {
            rvWhiteCards.setVisibility(View.GONE);
            llMessageToPlayer.setVisibility(View.VISIBLE);

            getActivity().setTitle(getActivity().getString(R.string.msg_you_are_the_king));
            tvMessageToPlayer.setText(getActivity().getString(R.string.msg_you_are_the_king_extended));
            ivMessageToPlayer.setImageResource(R.drawable.ic_mood_grey_700_48dp);

            rvWhiteCards.setAdapter(new WhiteCardsAdapter(getActivity(), new ArrayList<JsonCard>(), null, 0));
        } else {
            rvWhiteCards.setVisibility(View.VISIBLE);
            llMessageToPlayer.setVisibility(View.GONE);
            getActivity().setTitle(String.format(getActivity().getString(R.string.dynamic_pick_cards),
                    jsonGameStage1Data.blackCard.answersCount,
                    jsonGameStage1Data.blackCard.answersCount > 1 ? getActivity().getString(R.string.text_s) : ""));
        }

        tvBlackCard.setText(jsonGameStage1Data.blackCard.text);
        JsonPlayersWhiteDeck playerWhiteCards = null;
        for (JsonPlayersWhiteDeck deck : jsonGameStage1Data.playersWhiteDecks) {
            if (deck.playerId.equals(networkManager.thisDevice().instanceName)) {
                playerWhiteCards = deck;
                break;
            }
        }

        if (playerWhiteCards != null) {
            rvWhiteCards.setAdapter(new WhiteCardsAdapter(
                    getActivity(),
                    Arrays.asList(playerWhiteCards.whiteCards),
                    selectedCardsCallback,
                    jsonGameStage1Data.blackCard.answersCount));
        } else {
            Log.d(App.TAG, "There is no white cards for player. Fix it.");
        }

    }

    @Override
    public void makeStage3Updates(JsonGameStage3Data jsonGameStage3Data, IClientStageCallback stageCallback) {
        this.stageCallback = stageCallback;

        if (!isKingNow) {
            Log.d(App.TAG, "makeStage3Updates: player is not the king. skipping the stage");
            return;
        }

        List<JsonWhiteCardsSelection> selections = Arrays.asList(jsonGameStage3Data.whiteCardsSelections);

        rvWhiteCards.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvWhiteCards.setAdapter(new SelectionAdapter(selections, selectedSelectionCallback));

        rvWhiteCards.setVisibility(View.VISIBLE);
        llMessageToPlayer.setVisibility(View.GONE);
    }

    void sendStage2Data(ArrayList<JsonCard> selectedCards) {
        // logic
        JsonGameStage2Data stage2Data = new JsonGameStage2Data();
        stage2Data.whiteCardsSelection = new JsonWhiteCardsSelection();
        stage2Data.whiteCardsSelection.playerId = networkManager.thisDevice().instanceName;
        stage2Data.whiteCardsSelection.selectedWhiteCards = selectedCards.toArray(new JsonCard[selectedCards.size()]);

        if (stageCallback != null) {
            stageCallback.stage2_send_white_cards_selection(stage2Data);
        }

        // UI
        getActivity().setTitle(getActivity().getString(R.string.msg_sent_to_the_king));
        tvMessageToPlayer.setText(getActivity().getString(R.string.msg_you_selected_cards));
        ivMessageToPlayer.setImageResource(R.drawable.ic_group_grey_700_48dp);

        rvWhiteCards.setVisibility(View.GONE);
        llMessageToPlayer.setVisibility(View.VISIBLE);
    }

    void sendStage4Data(JsonWhiteCardsSelection selectedSelection) {
        // logic
        JsonGameStage4Data stage4Data = new JsonGameStage4Data();
        stage4Data.whiteCardsSelection = selectedSelection;

        if (stageCallback != null) {
            stageCallback.stage4_send_selected_round_winner(stage4Data);
        }

        // UI
        if (!networkManager.isDeviceHost()) {
            tvMessageToPlayer.setText(getActivity().getString(R.string.msg_you_selected_selection));
            ivMessageToPlayer.setImageResource(R.drawable.ic_gesture_grey_700_48dp);

            rvWhiteCards.setVisibility(View.GONE);
            llMessageToPlayer.setVisibility(View.VISIBLE);
        }
    }

    boolean isCurrentPlayerKing(JsonPlayerState[] playerStates) {
        for (JsonPlayerState p : playerStates) {
            if (p.isKing && p.id.equals(networkManager.thisDevice().instanceName)) {
                return true;
            }
        }

        return false;
    }
}
