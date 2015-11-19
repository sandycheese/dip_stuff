package com.humanity.vs.cards.cardsvshumanity.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.GameClient;
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.LobbyClientsAdapter;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;
import com.humanity.vs.cards.cardsvshumanity.utils.ErrorsHelper;
import com.humanity.vs.cards.cardsvshumanity.utils.FragmentsHelper;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;

/**
 * Created by robot on 17.11.15.
 */
// todo make kick function?
public class GameLobbyFragment extends Fragment {

    public static final String ARG_START_AS_HOST = "ARG_START_AS_HOST";

    private NetworkManager networkManager;

    RecyclerView rvLobbyClients;
    View dividerForButton;
    Button btnStartGame;
    Button btnTestSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_lobby, container, false);

        getActivity().setTitle(getActivity().getString(R.string.title_game_lobby));

        findComponents(view);

        if (initNetworkManager()) {
            boolean startAsHost = false;

            Bundle bundle = getArguments();

            if (bundle != null)
                startAsHost = bundle.getBoolean(ARG_START_AS_HOST);

            if (startAsHost)
                startHostMode();
            else
                startClientMode();

            initComponents(startAsHost);

            String extendedTitle = String.format("%s (%s)",
                    getActivity().getString(R.string.title_game_lobby),
                    startAsHost ?
                            getActivity().getString(R.string.text_host) :
                            getActivity().getString(R.string.text_client));

            getActivity().setTitle(extendedTitle);
        }

        return view;
    }

    private void startHostMode() {
        networkManager.startHost(new SalutCallback() {
            @Override
            public void call() {
                ArrayList<SalutDevice> allClients = networkManager.getAllClients();
                ArrayList<GameClient> gameClients = new ArrayList<>();
                for (SalutDevice device : allClients) {
                    GameClient gameClient = new GameClient(device.instanceName, device.deviceName);
                    gameClients.add(gameClient);
                }

                rvLobbyClients.setAdapter(new LobbyClientsAdapter(gameClients));

                networkManager.updateLobbyForClients(gameClients);
            }
        }, new SalutCallback() {
            @Override
            public void call() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.text_sorry);
                builder.setMessage(R.string.msg_unable_create_host);
                builder.setCancelable(false);
                builder.setPositiveButton(R.string.text_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FragmentsHelper.setFragment(getActivity(), R.id.rlContainer, new GamesOnlineFragment());
                    }
                });
                builder.show();
            }
        });
    }

    private void startClientMode() {

    }

    void findComponents(View v) {
        rvLobbyClients = (RecyclerView) v.findViewById(R.id.rvLobbyClients);
        rvLobbyClients.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnStartGame = (Button) v.findViewById(R.id.btnStartGame);
        dividerForButton = v.findViewById(R.id.dividerForStartGameButton);

        btnTestSend = (Button) v.findViewById(R.id.btnTestSend);
    }

    private void initComponents(boolean startAsHost) {
        btnStartGame.setVisibility(startAsHost ? View.VISIBLE : View.GONE);
        dividerForButton.setVisibility(startAsHost ? View.VISIBLE : View.GONE);

        btnTestSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkManager.sendPingToFirstDevice();
            }
        });
    }

    boolean initNetworkManager() {
        Activity a = getActivity();

        if (a instanceof INetworkManagerProvider) {
            networkManager = ((INetworkManagerProvider) a).getNetworkManager();
            return true;
        } else {
            ErrorsHelper.commonError(a);
            FragmentsHelper.setFragment(a, R.id.rlContainer, new GamesOnlineFragment());
            return false;
        }
    }
}
