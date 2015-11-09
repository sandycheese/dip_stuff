package com.humanity.vs.cards.cardsvshumanity.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 08.11.15.
 */
public class Player {
    String id;
    String nickname;
    int orderInRound;
    List<Card> cards;

    public Player(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;

        this.cards = new ArrayList<>();
    }

    public void setOrderInRound(int orderInRound) {
        this.orderInRound = orderInRound;
    }
}
