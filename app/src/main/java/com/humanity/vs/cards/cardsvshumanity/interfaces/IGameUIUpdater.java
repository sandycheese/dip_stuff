package com.humanity.vs.cards.cardsvshumanity.interfaces;

import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage1Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage3Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;

/**
 * Created by robot on 08.11.15.
 */
public interface IGameUIUpdater {
    void makeStage1Updates(JsonGameStage1Data jsonGameStage1Data, IClientStageCallback clientStageCallback, String playerId);

    void makeStage3Updates(JsonGameStage3Data jsonGameStage3Data, IClientStageCallback stageCallback, String playerId);
}
