package com.humanity.vs.cards.cardsvshumanity.enums;

/**
 * Created by robot on 08.11.15.
 */
public enum NetworkGameCommand {
    net_step0_host_updatePlayersState,
    net_step1_host_resupplyPlayerCards,
    net_step2_host_grantPermissionToChooseWhiteCards,
    net_step3_client_sendBackSelectedWhiteCards,
    net_step4_host_grantPermissionToChooseRoundWinner,
    net_step5_client_sendBackRoundWinner
}
