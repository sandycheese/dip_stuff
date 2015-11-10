package com.humanity.vs.cards.cardsvshumanity.managers;

import com.google.gson.Gson;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.entities.Player;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.helpers.GameManagerHelper;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsHandler;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robot on 07.11.15.
 */
// todo null & empty checks everywhere? is it worth it?
// todo add errors texts
// todo game will absolutely not cheat-protected (code simplicity over complexity. i don't like mind blowing when i read code)
public class GameManager implements INetworkGameCommandsHandler {

    private List<Player> players;
    private List<Card> cards;
    private MatchRules matchRules;
    private INetworkGameCommandsSender networkCommandsSender;
    private boolean isHost;

    public void newGameAsHost(INetworkGameCommandsSender networkGameCommandsSender, List<GameClient> gameClients, List<Card> cards, MatchRules matchRules) {
        this.networkCommandsSender = networkCommandsSender;
        this.isHost = false;

        this.cards = cards;
        this.matchRules = matchRules;

        if (!isGameDataOk(gameClients, cards, matchRules)) {
            error();
            return;
        }

        createPlayersList(gameClients);
        definePlayersOrder();
        startGameReactiveLoop();
    }

    public void newGameAsClient(INetworkGameCommandsSender networkGameCommandsSender) {
        this.networkCommandsSender = networkCommandsSender;
        this.isHost = false;
    }

    private boolean isGameDataOk(List<GameClient> gameClients, List<Card> cards, MatchRules matchRules) {
        if (EmptyUtils.isEmpty(gameClients) || EmptyUtils.isEmpty(cards))
            return false;

        if (matchRules.scoreToWin < 1)
            return false;

        return true;
    }

    private void createPlayersList(List<GameClient> gameClients) {
        this.players = new ArrayList<>();

        for (GameClient gameClient : gameClients) {
            this.players.add(new Player(gameClient.getId(), gameClient.getNickname()));
        }
    }

    private void definePlayersOrder() {
        ArrayList<Integer> orderNumbers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            orderNumbers.add(i);
        }

        Collections.shuffle(orderNumbers);

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setOrderInRound(orderNumbers.get(i));
        }
    }

    private void startGameReactiveLoop() {
        networkGameStage1_host_cmd();
    }

    private void networkGameStage1_host_cmd() {
        JsonGameStage1Data data = GameManagerHelper.getStage1Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage1_client_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage1, jsonData);
    }

    private void networkGameStage1_client_handler(JsonGameStage1Data jsonGameStage1Data) {

    }

    private void networkGameStage2_client_cmd() {
        JsonGameStage1Data data = GameManagerHelper.getStage2Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage1_client_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage2, jsonData);
    }

    private void networkGameStage2_host_handler(JsonGameStage2Data jsonGameStage2Data) {

    }

    private void networkGameStage3_host_cmd() {
        JsonGameStage1Data data = GameManagerHelper.getStage3Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage1_client_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage3, jsonData);
    }

    private void networkGameStage3_client_handler(JsonGameStage3Data jsonGameStage3Data) {

    }

    private void networkGameStage4_client_cmd() {
        JsonGameStage1Data data = GameManagerHelper.getStage4Data(this);
        String jsonData = new Gson().toJson(data, JsonGameStage1Data.class);

        if (isHost)
            networkGameStage1_client_handler(data);
        else
            networkCommandsSender.sendNetworkGameCommand(NetworkGameCommand.gameStage4, jsonData);
    }

    private void networkGameStage4_host_handler(JsonGameStage4Data jsonGameStage4Data) {

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

}