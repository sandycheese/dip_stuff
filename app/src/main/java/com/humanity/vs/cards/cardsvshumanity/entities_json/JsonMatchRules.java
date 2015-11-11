package com.humanity.vs.cards.cardsvshumanity.entities_json;

import com.humanity.vs.cards.cardsvshumanity.entities.MatchRules;

/**
 * Created by robot on 11.11.15.
 */
public class JsonMatchRules extends MatchRules {
    public int scoreToWin;


    public JsonMatchRules(int scoreToWin) {
        this.scoreToWin = scoreToWin;
    }

    public JsonMatchRules(MatchRules matchRules) {
        this.scoreToWin = matchRules.scoreToWin;
    }
}
