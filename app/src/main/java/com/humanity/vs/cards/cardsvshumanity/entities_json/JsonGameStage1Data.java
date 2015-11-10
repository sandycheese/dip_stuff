package com.humanity.vs.cards.cardsvshumanity.entities_json;

/**
 * Created by robot on 09.11.15.
 */
// network game stage where: shows a black card; resupplies white cards; updates the players list;
public class JsonGameStage1Data {
    public JsonCard blackCard;
    public JsonPlayersWhiteDeck[] playersWhiteDecks;
    public JsonPlayerState[] playerStates;
}
