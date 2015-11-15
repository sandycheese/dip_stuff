package com.humanity.vs.cards.cardsvshumanity.ui.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.adapters.HostsAdapter;
import com.humanity.vs.cards.cardsvshumanity.ui.entities.Host;

import java.util.ArrayList;

/**
 * Created by robot on 15.11.15.
 */
// todo create empty view for recycleview
// todo make possible set points to win
public class GamesOnlineFragment extends Fragment {

    FloatingActionButton fabCreateGame;
    FloatingActionButton fabUpdateHostsList;
    RecyclerView rvHosts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_games_online, container, false);

        initComponents(view);

        return view;
    }

    void initComponents(View v) {
        fabCreateGame = (FloatingActionButton) v.findViewById(R.id.fabCreateGame);
        fabUpdateHostsList = (FloatingActionButton) v.findViewById(R.id.fabUpdateHostsList);

        rvHosts = (RecyclerView) v.findViewById(R.id.rvHosts);
        rvHosts.setLayoutManager(new LinearLayoutManager(getActivity()));

        rvHosts.setAdapter(new HostsAdapter(new ArrayList<Host>()));
    }
}
