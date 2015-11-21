package com.humanity.vs.cards.cardsvshumanity.logic.entities_json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by robot on 11.11.15.
 */
@JsonObject
public class JsonParse_Card {
    @JsonField
    public int id;
    @JsonField
    public String cardType;
    @JsonField
    public String text;
    @JsonField
    public int numAnswers;
}
