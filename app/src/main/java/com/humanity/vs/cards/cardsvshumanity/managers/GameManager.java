package com.humanity.vs.cards.cardsvshumanity.managers;

import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.entities.Player;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerCards;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerRoundWinner;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerSelectedWhiteCards;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayersStates;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.helpers.GameManagerHelper;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsHandler;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robot on 07.11.15.
 */
// todo null & empty checks everywhere? is it worth it?
// todo add errors texts
public class GameManager implements INetworkGameCommandsHandler {

    private List<Player> players;
    private List<Card> cards;
    private MatchRules matchRules;
    private INetworkCommandsSender networkCommandsSender;

    public GameManager(INetworkCommandsSender networkCommandsSender) {

        this.networkCommandsSender = this.networkCommandsSender;
    }

    public void newGame(List<GameClient> gameClients, List<Card> cards, MatchRules matchRules) {
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
        networkCommandsSender.net_step0_host_updatePlayersState_cmd();
    }

    void error() {
        throw new UnsupportedOperationException();
    }


    public void net_step0_host_updatePlayersState_cmd() {

    }

    public void net_step1_host_resupplyPlayerCards_cmd() {

    }

    public void net_step2_host_grantPermissionToChooseWhiteCards_cmd() {

    }

    public void net_step3_client_sendBackSelectedWhiteCards_cmd() {

    }

    public void net_step4_host_grantPermissionToChooseRoundWinner_cmd() {

    }

    public void net_step5_client_sendBackRoundWinner_cmd() {

    }

    public void net_step0_host_updatePlayersState_handler(JsonPlayersStates jsonPlayersStates) {

    }

    public void net_step1_host_resupplyPlayerCards_handler(JsonPlayerCards jsonPlayerCards) {

    }

    public void net_step2_host_grantPermissionToChooseWhiteCards_handler() {

    }

    public void net_step3_client_sendBackSelectedWhiteCards_handler(JsonPlayerSelectedWhiteCards jsonPlayerSelectedWhiteCards) {

    }

    public void net_step4_host_grantPermissionToChooseRoundWinner_handler() {

    }

    public void net_step5_client_sendBackRoundWinner_handler(JsonPlayerRoundWinner jsonPlayerRoundWinner) {

    }

    void sendNetworkCommand(NetworkGameCommand networkGameCommand, String jsonData) {

    }

    @Override
    public void handleNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData) {
        switch (networkGameCommand) {
            case net_step0_host_updatePlayersState:
                JsonPlayersStates jsonPlayersStates = GameManagerHelper.getPlayerStatesFromJson(jsonData);
                net_step0_host_updatePlayersState_handler(jsonPlayersStates);
                break;
            case net_step1_host_resupplyPlayerCards:
                JsonPlayerCards jsonPlayerCards = GameManagerHelper.getPlayerCardsFromJson(jsonData);
                net_step1_host_resupplyPlayerCards_handler(jsonPlayerCards);
                break;
            case net_step2_host_grantPermissionToChooseWhiteCards:
                net_step2_host_grantPermissionToChooseWhiteCards_handler();
                break;
            case net_step3_client_sendBackSelectedWhiteCards:
                JsonPlayerSelectedWhiteCards jsonPlayerSelectedWhiteCards = GameManagerHelper.getPlayerSelectedWhiteCardsFromJson(jsonData);
                net_step3_client_sendBackSelectedWhiteCards_handler(jsonPlayerSelectedWhiteCards);
                break;
            case net_step4_host_grantPermissionToChooseRoundWinner:
                net_step4_host_grantPermissionToChooseRoundWinner_handler();
                break;
            case net_step5_client_sendBackRoundWinner:
                JsonPlayerRoundWinner jsonPlayerRoundWinner = GameManagerHelper.getPlayerRoundWinner(jsonData);
                net_step5_client_sendBackRoundWinner_handler(jsonPlayerRoundWinner);
                break;
        }
    }
}