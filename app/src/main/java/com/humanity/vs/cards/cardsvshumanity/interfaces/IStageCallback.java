package com.humanity.vs.cards.cardsvshumanity.interfaces;

import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonRoundWinnerSelection;
import com.humanity.vs.cards.cardsvshumanity.entities_json.JsonWhiteCardsSelection;

/**
 * Created by robot on 10.11.15.
 */
public interface IStageCallback {
    void stage2_send_white_cards_selection(JsonWhiteCardsSelection jsonWhiteCardsSelection);

    void stage4_send_selected_round_winner(JsonRoundWinnerSelection jsonRoundWinnerSelection);
}
