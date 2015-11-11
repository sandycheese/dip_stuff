package com.humanity.vs.cards.cardsvshumanity.helpers;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.PlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonMatchRules;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayersWhiteDeck;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonRoundResult;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonWhiteCardsSelection;
import com.humanity.vs.cards.cardsvshumanity.interfaces.IHostStageCallback;
import com.humanity.vs.cards.cardsvshumanity.managers.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by robot on 08.11.15.
 */
public class GameManagerHelper {
    private static final String TAG = "ddd";

    private static final int PLAYER_CARDS_COUNT = 10;

    public static JsonGameStage1Data getStage1DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage1Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage2Data getStage2DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage2Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage3Data getStage3DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage3Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    public static JsonGameStage4Data getStage4DataFromJson(String jsonData) {
        try {
            return new Gson().fromJson(jsonData, JsonGameStage4Data.class);
        } catch (JsonSyntaxException e) {
            return null;
        }
    }

    // host only
    public static JsonGameStage1Data getStage1Data(GameManager gameManager) {
        JsonGameStage1Data data = new JsonGameStage1Data();

        List<Card> unusedCards = gameManager.getUnusedCards();

        // select a random black card
        List<Card> blackCards = getBlackCardsFromList(unusedCards);

        Card blackCard = blackCards.get(new Random().nextInt(blackCards.size()));
        unusedCards.remove(blackCard);

        gameManager.setCurrentBlackCard(new JsonCard(blackCard));

        data.blackCard = new JsonCard(blackCard);


        // resupply white decks of players
        List<Card> whiteCards = getWhiteCardsFromList(unusedCards);

        List<PlayerState> playersStates = gameManager.getPlayersStates();
        for (PlayerState p : playersStates) {
            while (p.cards.size() < PLAYER_CARDS_COUNT) {
                Card whiteCard = whiteCards.get(new Random().nextInt(whiteCards.size()));
                unusedCards.remove(whiteCard);

                p.cards.add(whiteCard);
            }
        }

        JsonPlayersWhiteDeck[] jsonPlayersWhiteDecks = new JsonPlayersWhiteDeck[playersStates.size()];
        for (PlayerState p : playersStates) {
            JsonPlayersWhiteDeck jsonPlayersWhiteDeck = new JsonPlayersWhiteDeck();
            jsonPlayersWhiteDeck.whiteCards = new JsonCard[PLAYER_CARDS_COUNT];

            for (int i = 0; i < jsonPlayersWhiteDeck.whiteCards.length; i++) {
                jsonPlayersWhiteDeck.whiteCards[i] = new JsonCard(p.cards.get(i));
            }

        }

        data.playersWhiteDecks = jsonPlayersWhiteDecks;


        // update players states
        JsonPlayerState[] jsonPlayerStates = new JsonPlayerState[playersStates.size()];
        for (int i = 0; i < jsonPlayerStates.length; i++) {
            jsonPlayerStates[i] = new JsonPlayerState(playersStates.get(i));
        }

        data.playerStates = jsonPlayerStates;


        // round result
        JsonRoundResult jsonRoundResult = gameManager.getRoundResult();
        data.roundResult = jsonRoundResult;


        // end game
        data.endGame = false;
        for (PlayerState p : playersStates) {
            if (p.score == gameManager.getMatchRules().scoreToWin) {
                data.endGame = true;
            }
        }


        return data;
    }

    // host only
    public static void handleStage2Data(GameManager gameManager, JsonGameStage2Data jsonGameStage2Data, IHostStageCallback hostStageCallback) {
        List<JsonWhiteCardsSelection> currentSelections = gameManager.getCurrentWhiteCardsSelections();
        List<PlayerState> playerStates = gameManager.getPlayersStates();

        if (currentSelections.size() >= playerStates.size()) {
            Log.d(TAG, "Invalid behavior");
            return;
        }

        // adding a player selection
        currentSelections.add(jsonGameStage2Data.whiteCardsSelection);

        // call a next stage if all done
        if (currentSelections.size() == playerStates.size()) {
            hostStageCallback.stage3_cmd();
        }
    }

    // host only
    public static JsonGameStage3Data getStage3Data(GameManager gameManager) {
        List<JsonWhiteCardsSelection> selections = gameManager.getCurrentWhiteCardsSelections();
        JsonGameStage3Data jsonGameStage3Data = new JsonGameStage3Data();
        jsonGameStage3Data.whiteCardsSelections = new JsonWhiteCardsSelection[selections.size()];
        for (int i = 0; i < selections.size(); i++) {
            jsonGameStage3Data.whiteCardsSelections[i] = selections.get(i);
        }

        selections.clear();

        return jsonGameStage3Data;
    }

    // host only
    public static void handleStage4Data(GameManager gameManager, JsonGameStage4Data jsonGameStage4Data, IHostStageCallback hostStageCallback) {
        String winnerId = jsonGameStage4Data.whiteCardsSelection.playerId;

        List<PlayerState> playerStates = gameManager.getPlayersStates();

        // adding a score
        for (PlayerState p : playerStates) {
            if (p.id.equals(winnerId)) {
                p.score++;
                break;
            }
        }

        // set round result
        JsonRoundResult jsonRoundResult = new JsonRoundResult();
        jsonRoundResult.blackCard = gameManager.getCurrentBlackCard();
        jsonRoundResult.kingSelectedWhiteCards = jsonGameStage4Data.whiteCardsSelection;
        gameManager.setRoundResult(jsonRoundResult);

        // start from beginning
        hostStageCallback.stage1_cmd();
    }

    public static List<Card> getBlackCardsFromList(List<Card> cards) {
        List<Card> blackCards = new ArrayList<>();
        for (Card c : cards) {
            if (c.isBlackCard)
                blackCards.add(c);
        }

        return blackCards;
    }

    public static List<Card> getWhiteCardsFromList(List<Card> cards) {
        List<Card> whiteCards = new ArrayList<>();
        for (Card c : cards) {
            if (!c.isBlackCard)
                whiteCards.add(c);
        }

        return whiteCards;
    }


}
