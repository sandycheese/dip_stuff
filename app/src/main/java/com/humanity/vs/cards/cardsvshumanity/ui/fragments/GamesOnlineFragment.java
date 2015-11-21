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
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.HostsAdapter;
import com.humanity.vs.cards.cardsvshumanity.ui.entities.JsonHost;
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
    FloatingActionButton fabUpdateHostsList;
    RecyclerView rvHosts;

    AlertDialog pdHostUpdating;
    AlertDialog pdConnectingToHost;
    AlertDialog adCantConnectToHost;

    SalutCallback onDiscoverHostsDoneHandler = new SalutCallback() {
        @Override
        public void call() {
            pdHostUpdating.dismiss();

            ArrayList<SalutDevice> devices = networkManager.getAllHosts();

            ArrayList<JsonHost> hosts = new ArrayList<>();
            for (SalutDevice device : devices) {
                JsonHost host = new JsonHost();
                host.hostName = device.readableName;
                host.deviceName = device.deviceName;
                host.salutDevice = device;

                hosts.add(host);
            }

            rvHosts.setAdapter(new HostsAdapter(hosts, networkManager, onStartRegistering, onSuccessfullyRegistered, onFailToRegister));
        }
    };

    SalutCallback onStartRegistering = new SalutCallback() {
        @Override
        public void call() {
            pdConnectingToHost.show();
        }
    };

    SalutCallback onSuccessfullyRegistered = new SalutCallback() {
        @Override
        public void call() {
            pdConnectingToHost.dismiss();
            FragmentsHelper.setFragment(getActivity(), R.id.rlContainer, new GameLobbyFragment());
        }
    };

    SalutCallback onFailToRegister = new SalutCallback() {
        @Override
        public void call() {
            // fixme exception here if afk
            pdConnectingToHost.dismiss();
            adCantConnectToHost.show();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_online, container, false);

        getActivity().setTitle(getActivity().getString(R.string.title_available_hosts));

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
        fabUpdateHostsList = (FloatingActionButton) v.findViewById(R.id.fabUpdateHostsList);

        rvHosts = (RecyclerView) v.findViewById(R.id.rvHosts);
        rvHosts.setLayoutManager(new LinearLayoutManager(getActivity()));

        pdHostUpdating = new ProgressDialog(getActivity());
        pdHostUpdating.setTitle(R.string.msg_updating_host_list);
        pdHostUpdating.setMessage(getString(R.string.text_please_wait));

        pdConnectingToHost = new ProgressDialog(getActivity());
        pdConnectingToHost.setTitle(R.string.msg_connectingToHost);
        pdConnectingToHost.setMessage(getString(R.string.text_please_wait));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle(R.string.dialog_title_connecting_to_host);
        builder.setMessage(R.string.msg_cant_connect_to_host);
        builder.setPositiveButton(R.string.text_ok, null);
        adCantConnectToHost = builder.create();
    }

    private void setButtonsListeners() {
        fabCreateGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putBoolean(GameLobbyFragment.ARG_START_AS_HOST, true);

                GameLobbyFragment gameLobbyFragment = new GameLobbyFragment();
                gameLobbyFragment.setArguments(bundle);

                FragmentsHelper.setFragment(getActivity(), R.id.rlContainer, gameLobbyFragment);
            }
        });
        fabUpdateHostsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdHostUpdating.show();
                networkManager.discoverHosts(onDiscoverHostsDoneHandler);
            }
        });
    }
}
