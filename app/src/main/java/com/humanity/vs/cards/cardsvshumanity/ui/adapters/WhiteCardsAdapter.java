package com.humanity.vs.cards.cardsvshumanity.ui.adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.humanity.vs.cards.cardsvshumanity.R;
import com.humanity.vs.cards.cardsvshumanity.logic.entities_json.JsonCard;

import java.util.List;

/**
 * Created by robot on 21.11.15.
 */
public class WhiteCardsAdapter extends RecyclerView.Adapter<WhiteCardsAdapter.WhiteCardsViewHolder> {

    private List<JsonCard> items;


    public WhiteCardsAdapter(List<JsonCard> cards) {
        this.items = cards;
    }

    @Override
    public WhiteCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_white_card, parent, false);

        return new WhiteCardsViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(WhiteCardsViewHolder holder, int position) {
        final JsonCard item = items.get(position);


        holder.tvCardText.setText(item.text);

        holder.cvGameCard.setTag(item.id);
        holder.cvGameCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public class WhiteCardsViewHolder extends RecyclerView.ViewHolder {

        CardView cvGameCard;
        TextView tvCardText;

        public WhiteCardsViewHolder(View itemView) {
            super(itemView);

            cvGameCard = (CardView) itemView.findViewById(R.id.cvGameCard);
            tvCardText = (TextView) itemView.findViewById(R.id.tvCardText);
        }
    }
}
