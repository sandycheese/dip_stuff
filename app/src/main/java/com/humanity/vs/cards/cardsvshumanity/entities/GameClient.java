package com.humanity.vs.cards.cardsvshumanity.entities;

import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;

/**
 * Created by robot on 07.11.15.
 */
public class GameClient {
    String id;
    String nickname;

    public GameClient(String id, String nickname) {
        this.id = id;
        this.nickname = nickname;

        if (EmptyUtils.isEmpty(id) || EmptyUtils.isEmpty(nickname))
            throw new NullPointerException();
    }

    public String getId() {
        return this.id;
    }

    public String getNickname() {
        return this.nickname;
    }

}
