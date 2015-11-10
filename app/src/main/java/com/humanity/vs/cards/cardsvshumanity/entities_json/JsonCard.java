package com.humanity.vs.cards.cardsvshumanity.entities_json;

import com.humanity.vs.cards.cardsvshumanity.entities.Card;

/**
 * Created by robot on 09.11.15.
 */
public class JsonCard {
    public String id;
    public String text;
    public int answersCount;
    public boolean isBlackCard;

    public JsonCard() {
    }

    public JsonCard(Card card) {
        id = card.id;
        text = card.text;
        answersCount = card.answersCount;
        isBlackCard = card.isBlackCard;
    }
}
