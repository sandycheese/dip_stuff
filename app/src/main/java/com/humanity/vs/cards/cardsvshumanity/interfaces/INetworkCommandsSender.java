package com.humanity.vs.cards.cardsvshumanity.interfaces;

/**
 * Created by robot on 08.11.15.
 */
public interface INetworkCommandsSender {
    void net_step0_host_updatePlayersState_cmd();
    void net_step1_host_resupplyPlayerCards_cmd();
    void net_step2_host_grantPermissionToChooseWhiteCards_cmd();
    void net_step3_client_sendBackSelectedWhiteCards_cmd();
    void net_step4_host_grantPermissionToChooseRoundWinner_cmd();
    void net_step5_client_sendBackRoundWinner_cmd();
}
