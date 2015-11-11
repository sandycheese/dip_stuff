package com.humanity.vs.cards.cardsvshumanity.interfaces;

import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage2Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonGameStage4Data;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonRoundWinnerSelection;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonWhiteCardsSelection;

/**
 * Created by robot on 10.11.15.
 */
public interface IStageCallback {
    void stage2_send_white_cards_selection(JsonGameStage2Data jsonGameStage2Data);

    void stage4_send_selected_round_winner(JsonGameStage4Data jsonGameStage4Data);
}
