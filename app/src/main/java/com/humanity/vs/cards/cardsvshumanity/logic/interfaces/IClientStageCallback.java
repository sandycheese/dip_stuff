package com.humanity.vs.cards.cardsvshumanity.logic.interfaces;

import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonGameStage4Data;

/**
 * Created by robot on 10.11.15.
 */
public interface IClientStageCallback {
    void stage2_send_white_cards_selection(JsonGameStage2Data jsonGameStage2Data);

    void stage4_send_selected_round_winner(JsonGameStage4Data jsonGameStage4Data);

    void endGame();
}
