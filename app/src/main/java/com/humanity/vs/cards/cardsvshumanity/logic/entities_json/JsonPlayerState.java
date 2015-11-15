package com.humanity.vs.cards.cardsvshumanity.logic.entities_json;

import com.humanity.vs.cards.cardsvshumanity.logic.entities.PlayerState;

/**
 * Created by robot on 08.11.15.
 */
public class JsonPlayerState {
    public String id;
    public String nickname;
    public int order;
    public int score;
    public boolean isKing;

    public JsonPlayerState() {
    }

    public JsonPlayerState(PlayerState playerState) {
        this.id = playerState.id;
        this.nickname = playerState.nickname;
        this.order = playerState.order;
        this.score = playerState.score;
        this.isKing = playerState.isKing;
    }
}
