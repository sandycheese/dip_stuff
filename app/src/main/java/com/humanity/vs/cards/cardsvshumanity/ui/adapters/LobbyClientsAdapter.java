package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities.GameClient;

import java.util.List;

/**
 * Created by robot on 17.11.15.
 */
public class LobbyClientsAdapter extends RecyclerView.Adapter<LobbyClientsAdapter.LobbyClientViewHolder> {
    List<GameClient> items;

    public LobbyClientsAdapter(List<GameClient> gameClients) {
        this.items = gameClients;
    }

    @Override
    public LobbyClientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lobby_client, parent, false);

        return new LobbyClientViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(LobbyClientViewHolder holder, int position) {
        GameClient item = items.get(position);

        holder.tvLobbyClientName.setText(String.format("%s", item.getNickname()));
    }

    public class LobbyClientViewHolder extends RecyclerView.ViewHolder {

        CardView cvLobbyClient;
        TextView tvLobbyClientName;

        public LobbyClientViewHolder(View itemView) {
            super(itemView);

            cvLobbyClient = (CardView) itemView.findViewById(R.id.cvLobbyClient);
            tvLobbyClientName = (TextView) itemView.findViewById(R.id.tvLobbyClientName);
        }
    }
}
