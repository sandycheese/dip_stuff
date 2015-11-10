package com.humanity.vs.cards.cardsvshumanity.interfaces;

import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;

/**
 * Created by robot on 08.11.15.
 */
public interface IGameUIUpdater {
    void makeStage1Updates(JsonGameStage1Data jsonGameStage1Data);
    void makeStage2Updates(JsonGameStage2Data jsonGameStage2Data);
    void makeStage3Updates(JsonGameStage3Data jsonGameStage3Data);
    void makeStage4Updates(JsonGameStage4Data jsonGameStage4Data);
}
