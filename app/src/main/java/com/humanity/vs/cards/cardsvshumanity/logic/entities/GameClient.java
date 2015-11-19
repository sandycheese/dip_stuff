package com.humanity.vs.cards.cardsvshumanity.logic.entities;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.humanity.vs.cards.cardsvshumanity.utils.EmptyUtils;

/**
 * Created by robot on 07.11.15.
 */
@JsonObject
public class GameClient {
    @JsonField
    String id;
    @JsonField
    String nickname;

    public GameClient() {
    }

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
