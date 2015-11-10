package com.humanity.vs.cards.cardsvshumanity.helpers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.humanity.vs.cards.cardsvshumanity.entities.Card;
import com.humanity.vs.cards.cardsvshumanity.entities.PlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonCard;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayerState;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonPlayersWhiteDeck;
import com.humanity.vs.cards.cardsvshumanity.managers.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by robot on 08.11.15.
 */
public class GameManagerHelper {

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


        return data;
    }

    public static JsonGameStage1Data getStage2Data(GameManager gameManager) {
        return null;
    }

    public static JsonGameStage1Data getStage3Data(GameManager gameManager) {
        return null;
    }

    public static JsonGameStage1Data getStage4Data(GameManager gameManager) {
        return null;
    }

    public static void handleStage1Data(GameManager gameManager, JsonGameStage1Data jsonGameStage1Data) {

    }

    public static void handleStage2Data(GameManager gameManager, JsonGameStage2Data jsonGameStage2Data) {

    }

    public static void handleStage3Data(GameManager gameManager, JsonGameStage3Data jsonGameStage3Data) {

    }

    public static void handleStage4Data(GameManager gameManager, JsonGameStage4Data jsonGameStage4Data) {

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
