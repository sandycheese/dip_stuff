package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.entities.JsonLobby;
import com.humanity.vs.cards.cardsvshumanity.ui.network.NetworkManager;
import com.peak.salut.Callbacks.SalutCallback;

import java.util.List;

/**
 * Created by robot on 15.11.15.
 */
public class LobbiesAdapter extends RecyclerView.Adapter<LobbiesAdapter.HostsViewHolder> {

    private List<JsonLobby> items;
    private NetworkManager networkManager;
    private SalutCallback startRegisteringCallback;
    private SalutCallback registerSuccessCallback;
    private SalutCallback registerFailCallback;

    public LobbiesAdapter(List<JsonLobby> hosts,
                          NetworkManager networkManager,
                          SalutCallback startRegisteringCallback, SalutCallback registerSuccessCallback,
                          SalutCallback registerFailCallback) {
        this.items = hosts;
        this.networkManager = networkManager;
        this.startRegisteringCallback = startRegisteringCallback;
        this.registerSuccessCallback = registerSuccessCallback;
        this.registerFailCallback = registerFailCallback;
    }

    @Override
    public HostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_host, parent, false);

        return new HostsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(HostsViewHolder holder, int position) {
        final JsonLobby item = items.get(position);

        holder.tvDeviceName.setText(item.deviceName);
        holder.tvHostShortInfo.setText(String.format("%s", item.serviceName));
        holder.cvHost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegisteringCallback.call();
                networkManager.registerWithHost(item.salutDevice, registerSuccessCallback, registerFailCallback);
            }
        });
    }

    public class HostsViewHolder extends RecyclerView.ViewHolder {

        CardView cvHost;
        TextView tvDeviceName;
        TextView tvHostShortInfo;

        public HostsViewHolder(View itemView) {
            super(itemView);

            cvHost = (CardView) itemView.findViewById(R.id.cvHost);
            tvDeviceName = (TextView) itemView.findViewById(R.id.tvDeviceName);
            tvHostShortInfo = (TextView) itemView.findViewById(R.id.tvHostShortInfo);
        }
    }
}
