package com.humanity.vs.cards.cardsvshumanity.ui.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.LobbiesAdapter;
import com.humanity.vs.cards.cardsvshumanity.ui.entities.JsonLobby;
import com.humanity.vs.cards.cardsvshumanity.ui.interfaces.INetworkManagerProvider;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;
import com.humanity.vs.cards.cardsvshumanity.utils.ErrorsHelper;
import com.humanity.vs.cards.cardsvshumanity.utils.FragmentsHelper;
import com.peak.salut.Callbacks.SalutCallback;
import com.peak.salut.SalutDevice;

import java.util.ArrayList;

/**
 * Created by robot on 15.11.15.
 */
// todo create empty view for recycleview
// todo make possible set points to win
// todo make connection progress dialog async
public class GamesOnlineFragment extends Fragment {

    NetworkManager networkManager;

    FloatingActionButton fabCreateGame;
    FloatingActionButton fabUpdateLobbiesList;
    RecyclerView rvLobbies;

    AlertDialog pdLobbiesUpdating;
    AlertDialog pdConnectingToLobby;
    AlertDialog adCantConnectToLobby;

    SalutCallback onDiscoverLobbiesDoneHandler = new SalutCallback() {
        @Override
        public void call() {
            pdLobbiesUpdating.dismiss();

            ArrayList<SalutDevice> devices = networkManager.getAllDiscoveredDevices();

            ArrayList<JsonLobby> lobbies = new ArrayList<>();
            for (SalutDevice device : devices) {
                JsonLobby lobby = new JsonLobby();
                lobby.serviceName = device.readableName;
                lobby.deviceName = device.deviceName;
                lobby.salutDevice = device;

                lobbies.add(lobby);
            }

            rvLobbies.setAdapter(new LobbiesAdapter(lobbies, networkManager, onStartRegistering, onSuccessfullyRegistered, onFailToRegister));
        }
    };

    SalutCallback onStartRegistering = new SalutCallback() {
        @Override
        public void call() {
            pdConnectingToLobby.show();
        }
    };

    SalutCallback onSuccessfullyRegistered = new SalutCallback() {
        @Override
        public void call() {
            pdConnectingToLobby.dismiss();
            FragmentsHelper.setFragment(getActivity(), R.id.rlContainer, new GameLobbyFragment());
        }
    };

    SalutCallback onFailToRegister = new SalutCallback() {
        @Override
        public void call() {
            // fixme exception here if afk
            pdConnectingToLobby.dismiss();
            adCantConnectToLobby.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_online, container, false);

        getActivity().setTitle(getActivity().getString(R.string.title_available_services));

        initComponents(view);
        initNetworkHostsListener();
        setButtonsListeners();

        return view;
    }

    private void initNetworkHostsListener() {
        Activity a = getActivity();

        if (a instanceof INetworkManagerProvider) {
            networkManager = ((INetworkManagerProvider) a).getNetworkManager();
        } else {
            ErrorsHelper.commonError(a);
            FragmentsHelper.setFragment(a, R.id.rlContainer, new GamesOnlineFragment());
        }
    }

    void initComponents(View v) {
        fabCreateGame = (FloatingActionButton) v.findViewById(R.id.fabCreateGame);
        fabUpdateLobbiesList = (FloatingActionButton) v.findViewById(R.id.fabUpdateHostsList);

        rvLobbies = (RecyclerView) v.findViewById(R.id.rvHosts);
        rvLobbies.setLayoutManager(new LinearLayoutManager(getActivity()));

        pdLobbiesUpdating = new ProgressDialog(getActivity());
        pdLobbiesUpdating.setTitle(R.string.msg_updating_host_list);
        pdLobbiesUpdating.setMessage(getString(R.string.text_please_wait));

        pdConnectingToLobby = new ProgressDialog(getActivity());
        pdConnectingToLobby.setTitle(R.string.msg_connectingToHost);
        pdConnectingToLobby.setMessage(getString(R.string.text_please_wait));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_connecting_to_lobby);
        builder.setMessage(R.string.msg_cant_connect_to_lobby);
        builder.setPositiveButton(R.string.text_ok, null);
        adCantConnectToLobby = builder.create();
    }

    private void setButtonsListeners() {
        fabCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(GameLobbyFragment.ARG_START_SERVICE, true);

                GameLobbyFragment gameLobbyFragment = new GameLobbyFragment();
                gameLobbyFragment.setArguments(bundle);

                FragmentsHelper.setFragment(getActivity(), R.id.rlContainer, gameLobbyFragment);
            }
        });
        fabUpdateLobbiesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdLobbiesUpdating.show();
                networkManager.discoverHosts(onDiscoverLobbiesDoneHandler);
            }
        });
    }
}
