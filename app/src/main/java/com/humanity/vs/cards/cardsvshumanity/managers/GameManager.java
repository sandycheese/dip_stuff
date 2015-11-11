package com.humanity.vs.cards.cardsvshumanity.managers;

import android.util.Log;

import com.google.gson.Gson;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.entities.PlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonRoundResult;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonWhiteCardsSelection;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommandDirection;
import com.humanity.vs.cards.cardsvshumanity.helpers.GameManagerHelper;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IGameUIUpdater;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IHostStageCallback;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsHandler;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IClientStageCallback;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robot on 07.11.15.
 */
// todo null & empty checks everywhere? is it worth it?
// todo add errors texts
// todo game will absolutely not cheat-protected (code simplicity over complexity. i don't like a mind blowing when i read a code)
public class GameManager implements INetworkGameCommandsHandler {

    private static final String TAG = "ddd";

    private List<PlayerState> playerStates;
    private List<Card> cards;
    private List<Card> unusedCards;
    private MatchRules matchRules;
    private INetworkGameCommandsSender networkCommandsSender;
    private IGameUIUpdater gameUIUpdater;
    private boolean isHost;
    private List<PlayerState> playersStates;
    private List<JsonWhiteCardsSelection> currentWhiteCardsSelections = new ArrayList<>();

    private IClientStageCallback clientStageCallback = new IClientStageCallback() {
        @Override
        public void stage2_send_white_cards_selection(JsonGameStage2Data jsonGameStage2Data) {
            networkGameStage2_client_cmd(jsonGameStage2Data);
        }

        @Override
        public void stage4_send_selected_round_winner(JsonGameStage4Data jsonGameStage4Data) {
            networkGameStage4_client_cmd(jsonGameStage4Data);
        }

        @Override
        public void endGame() {
            endGame();
        }
    };
    private IHostStageCallback hostStageCallback = new IHostStageCallback() {
        @Override
        public void stage1_cmd() {
            networkGameStage1_host_cmd();
        }

        @Override
        public void stage3_cmd() {
            networkGameStage3_host_cmd();
        }
    };
    private JsonCard currentBlackCard;
    private JsonRoundResult roundResult;

    public void newGameAsHost(INetworkGameCommandsSender networkGameCommandsSender, IGameUIUpdater gameUIUpdater, List<GameClient> gameClients, List<Card> cards, MatchRules matchRules) {
        this.networkCommandsSender = networkCommandsSender;
        this.gameUIUpdater = gameUIUpdater;

        this.isHost = false;

        this.cards = cards;
        this.unusedCards = new ArrayList<>(cards);
        this.matchRules = matchRules;

        if (!isGameDataOk(gameClients, cards, matchRules)) {
            error();
            return;
        }

        initPlayersGameStates(gameClients);

        startGameReactiveLoop();
    }

    public void newGameAsClient(INetworkGameCommandsSender networkGameCommandsSender, IGameUIUpdater gameUIUpdater) {
        this.networkCommandsSender = networkCommandsSender;
        this.gameUIUpdater = gameUIUpdater;

        this.isHost = false;
    }

    private boolean isGameDataOk(List<GameClient> gameClients, List<Card> cards, MatchRules matchRules) {
        if (EmptyUtils.isEmpty(gameClients) || EmptyUtils.isEmpty(cards))
            return false;

        if (matchRules.scoreToWin < 1)
            return false;

        return true;
    }

    private void initPlayersGameStates(List<GameClient> gameClients) {
        this.playerStates = new ArrayList<>();

        // create players states list
        for (GameClient gameClient : gameClients) {
            this.playerStates.add(new PlayerState(gameClient.getId(), gameClient.getNickname()));
        }

        // define players order
        ArrayList<Integer> orderNumbers = new ArrayList<>();
        for (int i = 0; i < playerStates.size(); i++) {
            orderNumbers.add(i);
        }

        Collections.shuffle(orderNumbers);

        for (int i = 0; i < playerStates.size(); i++) {
            playerStates.get(i).order = orderNumbers.get(i);
        }

        // set score 0
        for (PlayerState p : playersStates) {
            p.score = 0;
        }

        // set the first king
        if (playersStates.size() > 0)
            playersStates.get(0).isKing = true;
    }

    private void startGameReactiveLoop() {
        networkGameStage1_host_cmd();
    }

    // STAGE 1: server sends a black card; resupplied white cards; an updated playerStates list; a round result; the end game;
    private void networkGameStage1_host_cmd() {
        if (!isHost) {
            Log.d(TAG, "It's host only method!");
            return;
        }

        JsonGameStage1Data data = GameManagerHelper.getStage1Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        networkGameStage1_client_handler(data);
        networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage1, jsonData, NetworkGameCommandDirection.toClients);
    }

    // STAGE 1: client shows a black card; white cards; players states; a round result; the end game;
    // calls stage2 (or finishes the game)
    private void networkGameStage1_client_handler(JsonGameStage1Data jsonGameStage1Data) {
        gameUIUpdater.makeStage1Updates(jsonGameStage1Data, clientStageCallback);
    }

    // STAGE 2: client sends back selected white cards;
    private void networkGameStage2_client_cmd(JsonGameStage2Data data) {
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage2_host_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage2, jsonData, NetworkGameCommandDirection.toHost);
    }

    // STAGE 2: server receives and handles selected white cards
    // calls stage3
    private void networkGameStage2_host_handler(JsonGameStage2Data jsonGameStage2Data) {
        GameManagerHelper.handleStage2Data(this, jsonGameStage2Data, hostStageCallback);
    }

    // STAGE 3: server sends chosen white cards;
    private void networkGameStage3_host_cmd() {
        if (!isHost) {
            Log.d(TAG, "It's host only method!");
            return;
        }

        JsonGameStage3Data data = GameManagerHelper.getStage3Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        networkGameStage3_client_handler(data);
        networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage3, jsonData, NetworkGameCommandDirection.toClients);
    }

    // STAGE 3: client shows chosen white cards; the king selects round winner cards
    // calls stage 4
    private void networkGameStage3_client_handler(JsonGameStage3Data jsonGameStage3Data) {
        gameUIUpdater.makeStage3Updates(jsonGameStage3Data, clientStageCallback);
    }

    // STAGE 4: client sends selected round winner cards;
    private void networkGameStage4_client_cmd(JsonGameStage4Data data) {
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage4_host_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage4, jsonData, NetworkGameCommandDirection.toHost);
    }

    // STAGE 4: server receives and handles round winner cards;
    // calls stage 1 (or end game)
    private void networkGameStage4_host_handler(JsonGameStage4Data jsonGameStage4Data) {
        GameManagerHelper.handleStage4Data(this, jsonGameStage4Data, hostStageCallback);
    }

    private void endGame() {

    }

    @Override
    public void handleNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData) {
        switch (networkGameCommand) {
            case gameStage1:
                JsonGameStage1Data jsonGameStage1Data = GameManagerHelper.getStage1DataFromJson(jsonData);
                networkGameStage1_client_handler(jsonGameStage1Data);
                break;
            case gameStage2:
                JsonGameStage2Data jsonGameStage2Data = GameManagerHelper.getStage2DataFromJson(jsonData);
                networkGameStage2_host_handler(jsonGameStage2Data);
                break;
            case gameStage3:
                JsonGameStage3Data jsonGameStage3Data = GameManagerHelper.getStage3DataFromJson(jsonData);
                networkGameStage3_client_handler(jsonGameStage3Data);
                break;
            case gameStage4:
                JsonGameStage4Data jsonGameStage4Data = GameManagerHelper.getStage4DataFromJson(jsonData);
                networkGameStage4_host_handler(jsonGameStage4Data);
                break;
        }
    }

    void error() {
        throw new UnsupportedOperationException();
    }

    public List<Card> getUnusedCards() {
        return this.unusedCards;
    }

    public List<PlayerState> getPlayersStates() {
        return this.playersStates;
    }

    public List<JsonWhiteCardsSelection> getCurrentWhiteCardsSelections() {
        return this.currentWhiteCardsSelections;
    }

    public MatchRules getMatchRules() {
        return this.matchRules;
    }

    public JsonCard getCurrentBlackCard() {
        return this.currentBlackCard;
    }

    public void setCurrentBlackCard(JsonCard currentBlackCard) {
        this.currentBlackCard = currentBlackCard;
    }

    public JsonRoundResult getRoundResult() {
        if (this.roundResult == null)
            return new JsonRoundResult();

        return this.roundResult;
    }

    public void setRoundResult(JsonRoundResult roundResult) {
        this.roundResult = roundResult;
    }
}