package com.humanity.vs.cards.cardsvshumanity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.App;
import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.entities.MatchRules;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayersWhiteDeck;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonWhiteCardsSelection;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommand;
import com.humanity.vs.cards.cardsvshumanity.enums.NetworkGameCommandDirection;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IClientStageCallback;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IGameUIUpdater;
import com.humanity.vs.cards.cardsvshumanity.interfaces.INetworkGameCommandsSender;
import com.humanity.vs.cards.cardsvshumanity.managers.GameManager;
import com.humanity.vs.cards.cardsvshumanity.repositories.CardsRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 11.11.15.
 */
public class TestsActivity extends Activity {
    GameManager gameManager1; // host, client
    GameManager gameManager2; // client
    GameManager gameManager3; // client

    LinearLayout llPlayer1;
    LinearLayout llPlayer2;
    LinearLayout llPlayer3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        emulateTreePlayerGame();
    }

    private void emulateTreePlayerGame() {
        initGameManagers();
    }

    private void initGameManagers() {
        llPlayer1 = (LinearLayout) findViewById(R.id.llPlayer1);
        llPlayer2 = (LinearLayout) findViewById(R.id.llPlayer2);
        llPlayer3 = (LinearLayout) findViewById(R.id.llPlayer3);

        gameManager1 = new GameManager();
        gameManager2 = new GameManager();
        gameManager3 = new GameManager();

        INetworkGameCommandsSender netSender1 = getNewINetworkGameCommandsSender("1");
        INetworkGameCommandsSender netSender2 = getNewINetworkGameCommandsSender("2");
        INetworkGameCommandsSender netSender3 = getNewINetworkGameCommandsSender("3");

        gameManager2.newGameAsClient(netSender2, getNewIGameUIUpdater(llPlayer2, netSender2.getClientNetworkId()));
        gameManager3.newGameAsClient(netSender3, getNewIGameUIUpdater(llPlayer3, netSender3.getClientNetworkId()));
        gameManager1.newGameAsHost(netSender1, getNewIGameUIUpdater(llPlayer1, netSender1.getClientNetworkId()), getGameClients(), getAllCards(), getMatchRules());
    }


    private INetworkGameCommandsSender getNewINetworkGameCommandsSender(final String clientNetworkId) {
        return new INetworkGameCommandsSender() {
            @Override
            public void sendNetworkGameCommand(NetworkGameCommand networkGameCommand, String jsonData, NetworkGameCommandDirection direction) {
                if (direction == NetworkGameCommandDirection.toHost) {
                    gameManager1.handleNetworkGameCommand(networkGameCommand, jsonData);
                } else if (direction == NetworkGameCommandDirection.toClients) {
                    gameManager1.handleNetworkGameCommand(networkGameCommand, jsonData);
                    gameManager2.handleNetworkGameCommand(networkGameCommand, jsonData);
                    gameManager3.handleNetworkGameCommand(networkGameCommand, jsonData);
                }

            }

            @Override
            public String getClientNetworkId() {
                return clientNetworkId;
            }

            @Override
            public void endGame() {

            }
        };
    }

    private IGameUIUpdater getNewIGameUIUpdater(final LinearLayout llOfPlayer, String clientId) {
        return new IGameUIUpdater() {
            @Override
            public void makeStage1Updates(final JsonGameStage1Data jsonGameStage1Data, final IClientStageCallback clientStageCallback, final String clientId) {
                Log.d(App.TAG, "makeStage1Updates: clientId = " + clientId);

                JsonPlayersWhiteDeck playerDeck = null;

                // components
                TextView tvPlayerOutput = null;
                EditText etPlayerInput = null;
                if (clientId.equals("1")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer1);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer1);
                }
                if (clientId.equals("2")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer2);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer2);
                }
                if (clientId.equals("3")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer3);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer3);
                }
                if (tvPlayerOutput == null || etPlayerInput == null)
                    return;

                // output
                String output = "";
                output += "END GAME: " + jsonGameStage1Data.endGame + "\n";
                output += "==============================\n";
                output += "STATES:\n";

                for (JsonPlayerState p : jsonGameStage1Data.playerStates) {
                    output += String.format("id=%s;name=%s;score:%s;isKing=%s;", p.id, p.nickname, p.score, p.isKing) + "\n";
                }
                output += "==============================\n";
                output += "BLACK CARD:\n";
                output += jsonGameStage1Data.blackCard.text + "\n";
                output += "==============================\n";
                output += "WHITE CARDS:\n";
                for (JsonPlayersWhiteDeck deck : jsonGameStage1Data.playersWhiteDecks) {
                    if (deck.playerId.equals(clientId)) {
                        playerDeck = deck;

                        for (JsonCard c : deck.whiteCards) {
                            output += c.id + " = " + c.text + "\n\n";
                        }
                    }
                }
                output += "==============================\n";

                tvPlayerOutput.setText(output);


                final EditText finalEtPlayerInput = etPlayerInput;
                final JsonPlayersWhiteDeck finalPlayerDeck = playerDeck;
                etPlayerInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d(App.TAG, "edit text key code: " + keyCode);

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            Log.d(App.TAG, "edit text input: " + finalEtPlayerInput.getText().toString());

                            JsonGameStage2Data jsonGameStage2Data = new JsonGameStage2Data();
                            JsonWhiteCardsSelection jsonWhiteCardsSelection = new JsonWhiteCardsSelection();
                            jsonWhiteCardsSelection.playerId = clientId;

                            String[] idsStrings = finalEtPlayerInput.getText().toString().split(" ");

                            if (finalPlayerDeck == null) {
                                Log.d(App.TAG, "Test error: player deck is null");
                                return false;
                            }

                            ArrayList<JsonCard> selectedCards = new ArrayList<>();
                            for (JsonCard c : finalPlayerDeck.whiteCards) {
                                String stringCardId = String.valueOf(c.id);

                                for (String inputId : idsStrings) {
                                    if (stringCardId.equals(inputId)) {
                                        selectedCards.add(c);
                                    }
                                }
                            }

                            jsonWhiteCardsSelection.selectedWhiteCards = new JsonCard[selectedCards.size()];
                            for (int i = 0; i < selectedCards.size(); i++) {
                                jsonWhiteCardsSelection.selectedWhiteCards[i] = selectedCards.get(i);
                            }

                            jsonGameStage2Data.whiteCardsSelection = jsonWhiteCardsSelection;

                            clientStageCallback.stage2_send_white_cards_selection(jsonGameStage2Data);

                            finalEtPlayerInput.setText("");

                            return true;
                        }

                        return false;
                    }
                });
            }

            @Override
            public void makeStage3Updates(JsonGameStage3Data jsonGameStage3Data, final IClientStageCallback stageCallback, final String clientId) {
                Log.d(App.TAG, "makeStage3Updates: clientId = " + clientId);

                // components
                TextView tvPlayerOutput = null;
                EditText etPlayerInput = null;
                if (clientId.equals("1")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer1);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer1);
                }
                if (clientId.equals("2")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer2);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer2);
                }
                if (clientId.equals("3")) {
                    tvPlayerOutput = (TextView) llOfPlayer.findViewById(R.id.tvOutputPlayer3);
                    etPlayerInput = (EditText) llOfPlayer.findViewById(R.id.etPlayer3);
                }
                if (tvPlayerOutput == null || etPlayerInput == null)
                    return;

                // output
                String output = "";
                output += "SELECTED CARDS:\n";

                for (JsonWhiteCardsSelection selection : jsonGameStage3Data.whiteCardsSelections) {
                    output += "PLAYER: " + selection.playerId + "\n";
                    for (JsonCard c : selection.selectedWhiteCards) {
                        output += c.id + " = " + c.text + "\n";
                    }
                }
                output += "==============================\n";

                tvPlayerOutput.setText(output);

                final JsonWhiteCardsSelection[] selections = jsonGameStage3Data.whiteCardsSelections;

                final EditText finalEtPlayerInput = etPlayerInput;
                etPlayerInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        Log.d(App.TAG, "edit text key code: " + keyCode);

                        if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                            Log.d(App.TAG, "edit text input: " + finalEtPlayerInput.getText().toString());

                            JsonGameStage4Data jsonGameStage4Data = new JsonGameStage4Data();
                            JsonWhiteCardsSelection selection = new JsonWhiteCardsSelection();

                            String selectedIdString = finalEtPlayerInput.getText().toString();

                            for (JsonWhiteCardsSelection cardsSelection : selections) {
                                if (selectedIdString.equals(cardsSelection.playerId)) {
                                    selection = cardsSelection;
                                    break;
                                }
                            }

                            jsonGameStage4Data.whiteCardsSelection = selection;

                            stageCallback.stage4_send_selected_round_winner(jsonGameStage4Data);

                            finalEtPlayerInput.setText("");

                            return true;
                        }

                        return false;
                    }
                });
            }
        };
    }

    private List<GameClient> getGameClients() {
        List<GameClient> gameClients = new ArrayList<>();
        gameClients.add(new GameClient("1", "Alice"));
        gameClients.add(new GameClient("2", "Bob"));
        gameClients.add(new GameClient("3", "Eve"));

        return gameClients;
    }

    private List<Card> getAllCards() {
        return CardsRepository.getAllCards(this);
    }

    private MatchRules getMatchRules() {
        MatchRules matchRules = new MatchRules();
        matchRules.scoreToWin = 5;
        return matchRules;
    }
}
