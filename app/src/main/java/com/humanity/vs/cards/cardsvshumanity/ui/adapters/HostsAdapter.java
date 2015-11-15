package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.ui.entities.Host;

import java.util.List;

/**
 * Created by robot on 15.11.15.
 */
public class HostsAdapter extends RecyclerView.Adapter<HostsAdapter.HostsViewHolder> {

    List<Host> items;

    public HostsAdapter(List<Host> hosts) {
        this.items = hosts;
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
        Host item = items.get(position);

        // todo make short time string
        holder.tvCreatedDate.setText(String.format("Created %s", item.createdTime.toString()));
        holder.tvHostShortInfo.setText(String.format("%s", item.hostName));
    }

    public class HostsViewHolder extends RecyclerView.ViewHolder {

        CardView cvHost;
        TextView tvCreatedDate;
        TextView tvHostShortInfo;

        public HostsViewHolder(View itemView) {
            super(itemView);

            cvHost = (CardView) itemView.findViewById(R.id.cvHost);
            tvCreatedDate = (TextView) itemView.findViewById(R.id.tvCreateDate);
            tvHostShortInfo = (TextView) itemView.findViewById(R.id.tvHostShortInfo);
        }
    }
}
