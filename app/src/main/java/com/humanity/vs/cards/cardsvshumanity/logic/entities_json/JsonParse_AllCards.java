package com.humanity.vs.cards.cardsvshumanity.logic.entities_json;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by robot on 11.11.15.
 */
@JsonObject
public class JsonParse_AllCards {
    @JsonField
    public JsonParse_Card[] masterCards;
}
