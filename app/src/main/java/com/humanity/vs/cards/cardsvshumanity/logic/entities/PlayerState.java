package com.humanity.vs.cards.cardsvshumanity.logic.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by robot on 08.11.15.
 */
public class PlayerState {
    public String id;
    public String nickname;
    public int order;
    public int score;
    public boolean isKing;

    public List<Card> cards;

    public PlayerState(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;
        this.cards = new ArrayList<>();
    }
}
